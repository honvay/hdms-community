app.controller('AppCtrl', ['$rootScope', '$scope', 'toaster', 'FileSystem',
    'Messager', '$http', '$modal', '$location', '$state',
    'environment', 'WebUploader', 'Dropbox',
    'FileOperation',
    function ($rootScope, $scope, toaster, FileSystem,
              Messager, $http, $modal, $location, $state,
              environment, WebUploader, Dropbox,
              FileOperation) {
        $scope.ie9 = /msie 9/.test(window.navigator.userAgent.toLowerCase());

        $scope.supportWebkitDirectory = ('webkitdirectory' in HTMLInputElement.prototype);
        $scope.showQueue = false;
        $rootScope.toaster = toaster;
        $rootScope.files = [];

        $rootScope.permissions = environment.permissions;
        $rootScope.space = environment.space;
        $rootScope.resourceServerUrl = "/";
        $rootScope.staticResourceServerUrl = "/";
        $rootScope.openDetail = true;
        $rootScope.appSetting = environment.appSetting;

        $rootScope.toggleDetail = function () {
            $rootScope.openDetail = !$rootScope.openDetail;
            $rootScope.$apply();
        }

        //文件上传
        WebUploader.init($rootScope);
        //拖拽上传
        Dropbox.init($scope);

        //文件操作
        FileOperation.init($scope);
        //initDropbox();

        //设置用户信息
        $rootScope.user = environment.user;
        $rootScope.organization = environment.user.organization;
        $rootScope.space = environment.space;
        $rootScope.space.quotaPrecent = $rootScope.space.used / $rootScope.space.quota;
        $rootScope.mounts = environment.mounts;
        $rootScope.mountAliases = {};
        for (var alias in $scope.mounts) {
            var mount = $scope.mounts[alias];
            $rootScope.mountAliases[mount.id] = mount.alias;
        }

        $scope.$on('$stateChangeStart', function (event, toState, stateParam, fromState) {
            $rootScope.module = toState.name;
            if ($scope.user.changePassword === true && toState.name !== 'changePassword') {
                event.preventDefault();
                $state.go("changePassword");
            }
        });


        $rootScope.mediaType = {
            avi: {icon: "asset/img/file/file_video.png", type: "video"},
            bmp: {icon: "asset/img/file/file_pic.png", type: "image"},
            png: {icon: "asset/img/file/file_pic.png", type: "image"},
            jpg: {icon: "asset/img/file/file_pic.png", type: "image"},
            gif: {icon: "asset/img/file/file_pic.png", type: "image"},
            csv: {icon: "asset/img/file/file_csv.png", type: "office"},
            doc: {icon: "asset/img/file/file_word.png", type: "office"},
            docx: {icon: "asset/img/file/file_word.png", type: "office"},
            xls: {icon: "asset/img/file/file_excel.png", type: "office"},
            xlsx: {icon: "asset/img/file/file_excel.png", type: "office"},
            ppt: {icon: "asset/img/file/file_ppt.png", type: "office"},
            pptx: {icon: "asset/img/file/file_ppt.png", type: "office"},
            pdf: {icon: "asset/img/file/file_pdf.png", type: "pdf"},
            exe: {icon: "asset/img/file/file_exe.png", type: "unknown"},
            html: {icon: "asset/img/file/file_code.png", type: "code"},
            xml: {icon: "asset/img/file/file_code.png", type: "code"},
            sql: {icon: "asset/img/file/file_code.png", type: "code"},
            java: {icon: "asset/img/file/file_code.png", type: "code"},
            js: {icon: "asset/img/file/file_code.png", type: "code"},
            jsp: {icon: "asset/img/file/file_code.png", type: "code"},
            md: {icon: "asset/img/file/file_code.png", type: "code"},
            cs: {icon: "asset/img/file/file_code.png", type: "code"},
            cpp: {icon: "asset/img/file/file_code.png", type: "code"},
            go: {icon: "asset/img/file/file_code.png", type: "code"},
            php: {icon: "asset/img/file/file_code.png", type: "code"},
            py: {icon: "asset/img/file/file_code.png", type: "code"},
            css: {icon: "asset/img/file/file_css.png", type: "code"},
            mp4: {icon: "asset/img/file/file_video.png", type: "video"},
            webm: {icon: "asset/img/file/file_video.png", type: "video"},
            mp3: {icon: "asset/img/file/file_music.png", type: "audio"},
            ogg: {icon: "asset/img/file/file_music.png", type: "audio"},
            wav: {icon: "asset/img/file/file_music.png", type: "audio"},
            txt: {icon: "asset/img/file/file_txt.png", type: "txt"},
            rar: {icon: "asset/img/file/file_zip.png", type: "unknown"},
            zip: {icon: "asset/img/file/file_zip.png", type: "unknown"},
            chm: {icon: "asset/img/file/file_chm.png", type: "unknown"}
        };

        $scope.onSearchKeyDown = function (event) {
            if (event.keyCode === 13) {
                $scope.search();
            }
        }

        $scope.search = function (event) {
            if ($scope.keyword) {
                $state.go("search", {
                    keyword: $scope.keyword
                });
            }
        }


        $scope.authorize = function (permission, file) {
            // 管理员在所有的目录下都可以进行操作 // 可以在我的文档中任意下载
            if ($scope.isAdmin() || $scope.mountAlias === 'my') {
                return true;
            }

            //根目录下只有下载
            if ($scope.router === 'enterprise') {
                if (permission === 'download') {
                    return true;
                }
                return false;
            }
            // 如果不根目录则用当前文件夹的权限来匹配
            if ($scope.current) {
                var permissions = $scope.current.permission;
                return !permissions || permissions.length === 0 || permissions.indexOf(permission) >= 0;
            } else {
                // 如果是根目录的化则要判断每一个选中的文件夹的权限
                var files = file ? [file] : $scope.selection;
                if (!files) return false;
                var permissions = $scope.permissions, len = files.length;
                if (!permissions) return true;
                for (var i = 0; i < len; i++) {
                    var filePermissions = permissions[files[i].id];
                    if (filePermissions && filePermissions.indexOf(permission) < 0) {
                        return false;
                    }
                }
                return true;
            }
        };

        //获取当前上下文
        $rootScope.getMountAlias = function (mount) {
            return $rootScope.mountAliases[mount];
        };

        //设置当前文件夹
        $rootScope.setCurrent = function (current) {
            if (current) {
                //挂载点
                $rootScope.mountAlias = $rootScope.mountAliases[current.mount];
                $rootScope.mount = $rootScope.mounts[$rootScope.mountAlias];
                $rootScope.router = current.id;
                $rootScope.driectoryName = current.name;
            } else {
                if ($rootScope.mountAlias === 'my') {
                    $rootScope.driectoryName = '个人文档';
                } else {
                    $rootScope.driectoryName = '企业文档';
                }
            }
            $rootScope.current = current;
        };

        //判断是否是管理员
        $rootScope.isAdmin = function () {
            return ['SYS_ADMIN', 'DOC_ADMIN'].indexOf($rootScope.user.role) >= 0;
        };

        $scope.$on('$locationChangeSuccess', function (event, fromUrl, toUrl) {
            if ($rootScope.fid) {
                $rootScope.openFile();
            } else {
                $rootScope.closeFile();
            }
        });

        $rootScope.getExt = function (file) {
            if (!file) return;
            var name = file.name;
            if (!name) {
                return;
            }
            return name.substring(name.lastIndexOf(".") + 1, name.length);
        };

        $rootScope.getIcon = function (file) {
            var icon;
            if (!file) {
                return null;
            }
            if (file.type === 'directory') {
                if (Util.isPrivate(file)) {
                    icon = 'asset/img/private_folder.png';
                } else {
                    icon = 'asset/img/public_folder.png';
                }
            } else {
                var ext = $rootScope.getExt(file);
                if (!ext) return;
                ext = ext.toLowerCase();
                //var ext = file.ext;
                var mediaType = $rootScope.mediaType[ext];
                if (mediaType) {
                    icon = mediaType.icon;
                } else {
                    icon = 'asset/img/file/file_unknown.png';
                }
            }
            return icon;
        };

        //更新用户设置
        $scope.updateUser = function () {
            $http.post("/user/set", {
                name: $scope.user.name,
                mode: $scope.user.mode,
                sortField: $scope.user.sortField,
                sortDesc: $scope.user.sortDesc
            });
        };

        $scope.sortField = $scope.user.sortField || 'name';
        $scope.sortFields = ['type', $scope.sortField];
        $scope.sortDesc = $scope.user.sortDesc || false;
        $scope.mode = $scope.user.mode || 'list';
        $scope.showTags = true;

        $scope.switchShowTag = function () {
            $scope.showTags = !$scope.showTags;
        }


        // FIXME 页面控制
        /** ********* 页面控制 *********** */
        $scope.switchMode = function (mode) {
            if (!mode) {
                $scope.mode = ($scope.mode === 'list' ? 'block' : 'list');
            } else {
                $scope.mode = mode;
            }
            $scope.user.mode = $scope.mode;
            //$scope.updateUser();
        };

        // 更改排序字段和规则
        $scope.changeSort = function (field) {
            if ($scope.sortField === field) {
                $scope.sortDesc = !$scope.sortDesc;
            } else {
                $scope.sortField = field;
                $scope.sortDesc = true;
            }
            $scope.sortFields = ['type', field];
            $scope.user.sortField = $scope.sortField;
            $scope.user.sortDesc = $scope.sortDesc;
            //$scope.updateUser();
        };

        // 更改排序规则
        $scope.changeDesc = function () {
            $scope.sortDesc = !$scope.sortDesc;
            $scope.user.sortDesc = $scope.sortDesc;
            //$scope.updateUser();
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


        /*$(window).on("click",function(event){
            if($(event.owner).closest(".queue").length == 0){
                $scope.showQueue = false;
            }
        });
        */

        if (environment.notice) {
            var notice = environment.notice;
            Messager.alert(notice.title, notice.content);
        }

        $rootScope.started = true;
    }]);

angular.element(document).ready(function () {

    loadEnvironment();

    //加载用户信息
    function loadEnvironment() {
        var xhr = $.ajax({
            url: "/environment",
            method: 'get'
        });
        xhr.success(function (result) {
            if (result.success) {
                delete result.success;
                app.value("environment", result.data);
                bootstrap();
            } else {
                alert("加载用户信息失败，请重新刷新页面再试");
            }
        }).error(function () {
            alert("加载用户信息失败，请重新刷新页面再试");
        });
    }

    function bootstrap() {
        angular.bootstrap(document, ['app']);
        window.clearInterval(window.preloadInterval);
    }
});