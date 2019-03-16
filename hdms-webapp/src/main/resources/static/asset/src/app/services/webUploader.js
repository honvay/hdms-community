app.factory('WebUploader', ['$rootScope', 'FileSystem', function ($rootScope, FileSystem) {

    var service = {};

    service.init = function ($scope) {

        $rootScope.onUploadFileChange = function () {
            var files = document.getElementById("uploadFile").files;
            if (!files) return;
            var len = files.length;
            for (var i = 0; i < len; i++) {
                var file = files[i];
                var fileId = WebUploader.guid();
                $scope.webUploader.addFile(file, fileId);
            }
        };

        //更新上传统计信息
        function updateUploadStats() {
            $scope.uploadStats = $scope.webUploader.getStats();
        }

        if (!window.WebUploader) {
            console.error("没有找到WebUploader组件");
            return;
        }

        //监听分块上传过程中的三个时间点
        WebUploader.Uploader.register({
            "before-send-file": "beforeSendFile",
            "before-send": "beforeSend",
            "after-send-file": "afterSendFile"
        }, {
            //时间点1：所有分块进行上传之前调用此函数
            beforeSendFile: function (file) {
                var deferred = WebUploader.Deferred();
                file.setStatus("md5", "正在校验文件");
                $scope.webUploader.md5File(file, 0, 10 * 1024 * 1024)
                    .progress(function (percentage) {
                        file.md5Percentage = percentage;
                    }).then(function (val) {
                    file.md5 = val;
                    //校验文件
                    FileSystem.verify(file).then(function (result) {
                        if (result.success) {
                            deferred.resolve();
                        } else {
                            file.setStatus("error", result.message || '文件校验失败');
                        }
                    }, function (result) {
                        file.setStatus("error", result.message);
                        deferred.reject();
                    });
                });
                return deferred.promise();
            }
        });

        var options = {
            swf: 'swf/Uploader.swf',
            server: '/fs/upload',
            auto: true,
            duplicate: false,
            compress: false,
            chunked: false,
            withCredentials: true,
            fileSizeLimit: parseInt($rootScope.appSetting.maxUploadFileSize) * 1024 * 1024
        };

        var uploader = WebUploader.create(options);

        uploader.on('uploadBeforeSend', function (block, data, headers) {
            data.md5 = block.file.md5;
            data.parent = block.file.parent;
            data.mount = block.file.mount;
            data.master = block.file.master;
            headers['X-Requested-With'] = 'XMLHttpRequest';
            headers[crsf.headerName] = crsf.token;
        });

        // 当有文件被添加进队列的时候
        uploader.on('fileQueued', function (file) {
            //拖拽上传的上下文
            if (file.source.source.uploadContext) {
                var uploadContext = file.source.source.uploadContext;
                file.mount = uploadContext.mount;
                file.mount = uploadContext.mount;
                file.parent = uploadContext.parent;
                file.path = uploadContext.path;
                file.mountAlias = uploadContext.mountAlias;
            } else {
                //普通的文件上传
                file.mount = $rootScope.mount.id;
                if ($rootScope.current) {
                    file.parent = $rootScope.current.id;
                    file.path = $rootScope.current.name;
                } else {
                    if ($rootScope.mountAlias === 'enterprise') {
                        file.path = '企业文档';
                    } else {
                        file.path = '我的文档';
                    }
                }
                file.mountAlias = $rootScope.mountAlias;
            }

            file.icon = $scope.getIcon(file);
            file.on("statuschange", function (status) {
                file.status = status;
            });
            $rootScope.files.push(file);
            $scope.showQueue = true;
            $scope.$apply();
        });

        uploader.on('fileDequeued', function (file) {
            var files = $rootScope.files;
            files.splice(files.indexOf(file), 1);
        });

        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            file.uploadPercentage = percentage.toFixed(2);
            file.setStatus("progress", '正在上传：' + file.uploadPercentage * 100 + "%");
            $scope.$apply();
        });

        uploader.on('uploadSuccess', function (file, response) {
            updateUploadStats();
            if (response.success === true) {
                $scope.$broadcast('$fileUploaded', response.data);
                file.setStatus("success", "上传成功");
            } else {
                file.setStatus("error", "上传失败");
            }
            $rootScope.$apply();
        });


        uploader.on('uploadError', function (file, source) {
            updateUploadStats();
            file.setStatus("error", source ? '系统异常，上传失败' : file.statusText);
            if (source) {
                $rootScope.$apply();
            }
        });

        uploader.on('uploadStart', function (file) {
            file.setStatus("progress", '正在上传');
            updateUploadStats();
        });

        uploader.on('uploadComplete', function (file) {
            updateUploadStats();
            if ($scope.uploadStats.progressNum === 0) {
                $scope.showQueue = false;
            }
        });
        $scope.webUploader = uploader;
    }
    return service;
}]);