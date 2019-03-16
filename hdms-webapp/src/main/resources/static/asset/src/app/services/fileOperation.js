app.factory('FileOperation', ['$rootScope', 'FileSystem', '$modal', 'Messager', '$state', function ($rootScope, FileSystem, $modal, Messager, $state) {

    var service = {};

    service.init = function ($scope) {   //关闭文件窗口

        $scope.openParent = function (file, event) {
            $state.go('directory', {
                directory: file.parent !== -1 ? file.parent : $scope.getMountAlias(file.mount)
            });
            event.stopPropagation();
        };

        $rootScope.open = function (file, event) {
            if (file.type === 'directory') {
                $state.go('directory', {
                    directory: file.id
                });
            } else {
                $rootScope.fid = file.id;
                $location.path("/index/document/" + file.id);
                if (event) {
                    event.stopPropagation();
                }
            }
        };

        $rootScope.closeFile = function (id) {
            if ($rootScope.fileModal) {
                $rootScope.fileModal.dismiss('cancel');
                $rootScope.fileModal = null;
            }
        };

        $rootScope.share = function (file) {
            Messager.alert("提示", "非常抱歉，社区版暂不支持该功能");
        };

        $rootScope.sendFile = function (files) {
            if (!angular.isArray(files)) {
                files = [files];
            }
            var modalInstance = $modal.open({
                templateUrl: 'asset/tpl/app/file_send.html',
                backdrop: 'static',
                keyboard: false,
                controller: 'FileSendCtrl',
                resolve: {
                    files: function () {
                        return files;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                if (result === true) {
                }
            }, function () {
            });
        };

        //打开文件窗口
        $rootScope.openFile = function (id) {
            var modalInstance = $modal.open({
                templateUrl: 'asset/tpl/app/document.html',
                backdrop: 'static',
                controller: 'FileDetailCtrl',
                size: 'full',
                resolve: {
                    id: function () {
                        return $rootScope.fid;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                if (result === true) {
                    $scope.$broadcast('$currentFolderReload', result.file);
                }
            }, function () {
            });
            $rootScope.fileModal = modalInstance;
            $rootScope.fid = null;
        };

        // 删除文件
        $rootScope.remove = function (files, success, error) {
            Messager.confirm('提示', '文件删除之后可以在回收站内还原，是否确定删除选中的文件?').then(
                function () {
                    var toast = $scope.toaster.wait({
                        title: '正在删除文件...'
                    });
                    FileSystem.remove(files).then(function (result) {
                        toast.doSuccess('删除文件成功');
                        if (angular.isFunction(success)) {
                            success(result);
                        }
                    }, function (cause) {
                        toast.doError('删除文件失败:' + cause);
                        if (angular.isFunction(error)) {
                            error(cause);
                        }
                    });
                });

        };

        $rootScope.lock = function (file, success, error) {
            Messager.confirm('提示', '锁定文件后只能本人进行解锁，确定要锁定?').then(
                function () {
                    var toast = $scope.toaster.wait({
                        title: '正在锁定...'
                    });
                    FileSystem.lock(file).then(function (result) {
                        toast.doSuccess('文件已锁定');
                        if (angular.isFunction(success)) {
                            success(result);
                        }
                    }, function (cause) {
                        toast.doError('锁定文件失败:' + cause);
                        if (angular.isFunction(error)) {
                            error(cause);
                        }
                    });
                });

        };

        $rootScope.unlock = function (file, success, error) {
            Messager.confirm('提示', '确定要解锁文件?').then(
                function () {
                    var toast = $scope.toaster.wait({
                        title: '正在解锁...'
                    });
                    FileSystem.unlock(file).then(function (result) {
                        toast.doSuccess('文件已解锁');
                        if (angular.isFunction(success)) {
                            success(result);
                        }
                    }, function (cause) {
                        toast.doError('解锁文件失败:' + cause);
                        if (angular.isFunction(error)) {
                            error(cause);
                        }
                    });
                });

        };

        $rootScope.setTag = function (file, success, error) {
            var modalInstance = $modal.open({
                templateUrl: 'asset/tpl/app/tags.html',
                controller: 'FileTagCtrl',
                backdrop: 'static',
                resolve: {
                    file: function () {
                        return file;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                if (angular.isFunction(success)) {
                    success(result);
                }
            });
        };

        $rootScope.rename = function (file, success, error) {
            Messager.prompt("重命名", "", file.name).then(function (name) {
                var toast = $scope.toaster.wait({
                    title: '正在重命名...'
                });
                FileSystem.rename(file.id, name).then(function (result) {
                    toast.doSuccess({
                        title: "重命名成功"
                    });
                    if (angular.isFunction(success)) {
                        success(result, name);
                    }
                }, function (cause) {
                    toast.doError({
                        title: "重命名失败",
                        body: cause
                    });
                    if (angular.isFunction(error)) {
                        error(cause);
                    }
                });
            });
        };


        $rootScope.move = function (files, mount, success, error) {
            var modalInstance = $modal.open({
                templateUrl: 'asset/tpl/app/move.html',
                controller: 'FileMoveCtrl',
                backdrop: 'static',
                resolve: {
                    data: function () {
                        return {
                            files: files,
                            mount: mount
                        };
                    }
                }
            });

            modalInstance.result.then(function (result) {
                var toast = $scope.toaster.wait({
                    title: '正在移动...'
                });
                FileSystem.move(files, result.id, result.mount).then(function (result) {
                    toast.doSuccess({
                        title: "移动成功"
                    });
                    if (angular.isFunction(success)) {
                        success(result);
                    }
                }, function (cause) {
                    toast.doError({
                        title: "移动失败",
                        body: cause
                    });
                    if (angular.isFunction(error)) {
                        error(cause);
                    }
                });
            });
        };

        $rootScope.copy = function (files, mount, error) {
            var modalInstance = $modal.open({
                templateUrl: 'asset/tpl/app/copy.html',
                controller: 'FileCopyCtrl',
                backdrop: 'static',
                resolve: {
                    data: function () {
                        return {
                            files: files,
                            mount: mount
                        };
                    }
                }
            });

            modalInstance.result.then(function (result) {
                var toast = $scope.toaster.wait({
                    title: '正在复制...'
                });
                FileSystem.copy(files, result.id, result.mount).then(function (result) {
                    toast.doSuccess({
                        title: "复制成功"
                    });
                    if (angular.isFunction(success)) {
                        success(result);
                    }
                }, function (cause) {
                    toast.doError({
                        title: "复制失败",
                        body: cause
                    });
                    if (angular.isFunction(error)) {
                        error(cause);
                    }
                });
            });
        };

        $rootScope.updateDescription = function (file, success, error) {
            var toast = $scope.toaster.wait({
                title: '正在修改备注...'
            });
            FileSystem.updateDescription(file).then(function (result) {
                toast.doSuccess({
                    title: "修改成功"
                });
                if (angular.isFunction(success)) {
                    success(result, name);
                }
            }, function (cause) {
                toast.doError({
                    title: "修改失败",
                    body: cause
                });
                if (angular.isFunction(error)) {
                    error(cause);
                }
            });
        }

        $rootScope.download = function (file) {
            $scope.downloadURL = "/fs/download?id=" + file.id;
        };

        //上传新版本
        $rootScope.uploadNewVersion = function (file) {
            Messager.alert("提示", "非常抱歉，社区版不支持文档多版本")
        };
    }
    return service;
}]);