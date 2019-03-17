app.controller('DirectoryCtrl',
    ['$rootScope', '$scope', '$http', '$state', '$location', '$modal', '$stateParams', 'Messager', 'FileSystem',
        function ($rootScope, $scope, $http, $state, $location,
                  $modal, $stateParams, Messager, FileSystem) {
            if ($scope.ie9) {
                $scope.webUploader.addButton({
                    id: '#filePicker',
                    innerHTML: '<i class="glyphicon glyphicon-cloud-upload"></i> 上传文件'
                });
            }

            $rootScope.module = "directory";
            var directory = $stateParams.directory;
            var mount = $rootScope.mounts[directory];
            if (mount) {
                $rootScope.mount = mount;
                $rootScope.mountAlias = directory;
                directory = null;
            }

            $scope.$on("$viewContentLoaded", function () {
                $scope.load(directory);
            });

            $scope.reload = function () {
                if ($scope.current) {
                    $scope.load($scope.current.id);
                } else {
                    $scope.load();
                }
            };

            /** *************** 加载文件 ****************** */
            $scope.load = function (id) {
                $scope.loading = true;
                $scope.reset();
                var mount = $scope.mount ? $scope.mount.id : null;
                FileSystem.load(mount, id).then(function (result) {
                        $scope.files = result.files;
                        $scope.path = result.path;
                        $scope.activities = result.activities;
                        $scope.authorizes = result.authorizes;
                        if (result.current) {
                            result.current.permissions = result.permissions;
                        }
                        $rootScope.setCurrent(result.current);
                        $scope.loading = false;
                    },
                    function (cause) {
                        $scope.error = cause;
                        $scope.toaster.pop("error", "加载文件失败", cause);
                        $scope.loading = false;
                    });
            };

            //这个权限模式会造成只有管理员才可以下载企业文档更目录的文件
            $scope.checkPermission = function (permission) {
                //我的文档
                if ($scope.mountAlias === 'my') {
                    return true;
                }

                //企业文档创建文件夹
                if (permission === 'create' && !$rootScope.current && $scope.mountAlias === 'enterprise') {
                    return true;
                }

                //企业文档上传文件夹
                if (permission === 'upload') {
                    if (!$rootScope.current && $scope.mountAlias === 'enterprise') {
                        return false;
                    }
                }
                if ($scope.current) {
                    if($scope.current.creationBy.id === $rootScope.user.id ){
                        return true;
                    }
                    if ($scope.current.permissions) {
                        return $scope.current.permissions.indexOf(permission) >= 0;
                    }
                }
                return false;
            };

            /**
             * 加载协作者
             */
            $scope.loadAuthorizes = function () {
                $http.get("/fs/authorize/list", {
                    params: {
                        documentId: $scope.current.id
                    }
                }).success(function (result, status, headers, config) {
                    $scope.loading = false;
                    if (result.success) {
                        $scope.authorizes = result.data;
                    } else {
                        $scope.toaster.pop('error', result.message);
                    }
                }).error(function (data, status) {
                    $scope.loading = false;
                    $scope.toaster.pop('error', '加载失败：' + status);
                });
            }

            $scope.$on('$fileUploaded', function (event, file) {
                if ($scope.current) {
                    if (file.parent = $scope.current.id) {
                        $scope.reload();
                    }
                } else {
                    if (file.mount === $scope.mount.id) {
                        $scope.reload();
                    }
                }
            });
            $scope.$on('$currentFolderReload', function (event, file) {
                $scope.reload();
            });

            $scope.addFavorite = function (file, event) {
                event.stopPropagation();
                FileSystem.addFavorite(file).then(function (result) {
                    $rootScope.toaster.success({
                        title: '已收藏'
                    });
                    file.favoriteId = result.data;
                }, function () {
                    $rootScope.toaster.error({
                        title: '收藏失败'
                    });
                });
            };

            $scope.removeFavorite = function (file, event) {
                event.stopPropagation();
                FileSystem.removeFavorite(file).then(function (result) {
                    $rootScope.toaster.success({
                        title: '收藏已取消'
                    });
                    file.favoriteId = null;
                }, function () {
                    $rootScope.toaster.error({
                        title: '取消收藏失败'
                    });
                });
            };

            /** ************** 权限控制 ************ */
            $scope.canShowAuthorizer = function (file) {
                if (file != null) {
                    return FileSystem.isDirectory(file) && $scope.mountAlias !== 'my';
                }
                return false;
            };

            /** ************** 权限控制 ************ */
            $scope.canEditPermission = function (file) {
                if (file != null) {
                    return file.creationBy.id === $rootScope.user.id;
                }
                return false;
            };

            $scope.isNonLocked = function (file) {
                return $scope.lockedSelectionCount === 0;
            };

            $scope.authorizeUnlock = function (file) {
                if ($scope.hasPermission('lock') === false) return false;
                var files = $scope.selection;
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    if (file.locked !== true || (file.lockedBy && file.lockedBy.id === $scope.user.id)) {
                        return false;
                    }
                }
                return true;
            };

            /** ********* 文件选择 *************** */
            $scope.isSelectAll = false;
            $scope.selection = [];
            //选中的文件中有多少是锁定的
            $scope.lockedSelectionCount = 0;

            $scope.selectAll = function () {
                $scope.isSelectAll = !$scope.isSelectAll;
                var len = $scope.files.length;
                for (var i = 0; i < len; i++) {
                    $scope.select($scope.files[i],
                        $scope.isSelectAll);
                }
            };

            $scope.addSelection = function (file) {
                $scope.selection.push(file);
                file.selected = true;
                if (file.locked === true) {
                    $scope.lockedSelectionCount++;
                }
                if ($scope.selection.length === $scope.files.length) {
                    $scope.isSelectAll = true;
                }
            };

            $scope.removeSelection = function (file) {
                $scope.selection.splice($scope.selection
                    .indexOf(file), 1);
                file.selected = false;
                if (file.locked === true) {
                    $scope.lockedSelectionCount++;
                }
                $scope.isSelectAll = false;
            };

            $scope.clearSelection = function () {
                $scope.selection.forEach(function (file) {
                    file.selected = false;
                });
                $scope.selection = [];
                $scope.isSelectAll = false;
            };

            $scope.select = function (file, selected, event) {
                if (event && $scope.mode === 'block' && !event.ctrlKey) {
                    $scope.clearSelection();
                }

                if (event) {
                    event.stopPropagation();
                }

                if (file.selected && selected === file.selected) {
                    return;
                }
                $(".app-ctrl").scrollTop(0);
                selected = selected || (file.selected ? !file.selected : true);
                return selected === false ? $scope.removeSelection(file) : $scope.addSelection(file);
            };

            $scope.removeAuthorize = function (authorize, event) {
                event.stopPropagation();
                Messager.confirm("提示", "确定要删除?").then(function () {
                    doRemove(authorize);
                });

                function doRemove(authorize) {
                    var toast = $scope.toaster.wait('正在删除...');
                    var http = $http.post('/fs/authorize/remove/' + authorize.id, {});
                    http.success(function (result) {
                        if (result.success) {
                            $scope.loadAuthorizes();
                            toast.doSuccess('已删除');
                        } else {
                            toast.doError('删除失败:' + result.message);
                        }
                    });
                    http.error(function (result, status) {
                        toast.doError('删除失败:' + (result.message || status));
                    });
                }
            };

            $scope.addAuthorize = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'asset/tpl/app/authorize_add.html',
                    controller: 'AuthorizeAddCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        permissions: function () {
                            return $scope.permissions;
                        },
                        id: function () {
                            return $scope.current.id;
                        }
                    }
                });

                modalInstance.result.then(function (users) {
                    $scope.loadAuthorizes();
                }, function () {

                });
            };

            $scope.updatePermission = function (permission) {

                if (permission.id !== $scope.selectedAuthorize.permissionId) {
                    Messager.confirm("提示", "确定要修改协作者的权限?").then(function () {
                        doUpdate();
                    });
                }

                function doUpdate() {
                    var toast = $scope.toaster.wait('正在修改权限...');
                    if ($scope.selectedAuthorize.inherited === true) {

                        var params = {
                            documentId: $scope.current.id,
                            permission: permission.id
                        }

                        if ($scope.selectedAuthorize.ownerType === 1) {
                            params.userIds = [$scope.selectedAuthorize.owner];
                        } else {
                            params.departmentIds = [$scope.selectedAuthorize.owner];
                        }

                        $http.post('/fs/authorize/add', params)
                            .success(function (result) {
                                if (result.success) {
                                    toast.doSuccess('权限已更新');
                                    $scope.loadAuthorizes();
                                } else {
                                    toast.doError('权限更新失败:' + result.message);
                                }
                            }).error(function (result, status) {
                            toast.doError('权限更新失败:' + (result.message || status));
                        });
                    } else {
                        $http.post('/fs/authorize/update', {
                            id: $scope.selectedAuthorize.id,
                            permissionId: permission.id
                        }).success(function (result) {
                            if (result.success) {
                                toast.doSuccess('权限已更新');
                                $scope.loadAuthorizes();
                            } else {
                                toast.doError('权限更新失败:' + result.message);
                            }
                        }).error(function (result, status) {
                            toast.doError('权限更新失败:' + (result.message || status));
                        });
                    }
                }
            }

            $scope.onContainerClick = function () {
                if ($scope.mode === 'block') {
                    $scope.clearSelection();
                }
            };

            $scope.filterTag = function (tag, event) {
                $scope.condition = tag;
                event.stopPropagation();
            }

            // FIXME 文件操作
            /** *************** 文件操作 *************** */
            // 下载文件
            $scope.download = function (file) {
                if (!file) {
                    file = $scope.selection[0];
                }
                $rootScope.download(file);
            };

            // 删除文件
            $scope.remove = function (file) {
                if (!file) {
                    file = $scope.selection;
                    if (file.length === 0) {
                        Messager.alert("提示", "请选择要删除的文件");
                    }
                }

                $rootScope.remove(file, function () {
                    $scope.reload();
                });
            };

            //锁定文件
            $scope.lock = function (file) {
                if (!file) {
                    file = $scope.selection[0];
                    if (!file) {
                        Messager.alert("提示", "请选择要锁定的文件");
                    }
                }

                $rootScope.lock(file, function () {
                    $scope.reload();
                });
            };
            //解锁文件
            $scope.unlock = function (file) {
                if (!file) {
                    file = $scope.selection[0];
                    if (!file) {
                        Messager.alert("提示", "请选择要解锁的文件");
                    }
                }

                $rootScope.unlock(file, function () {
                    $scope.reload();
                });
            };
            //重命名文件
            $scope.rename = function (file) {
                if (!file) {
                    file = $scope.selection[0];
                }
                if (!file) {
                    Messager.alert("提示", "请选择要重命名的文件");
                }
                $rootScope.rename(file, function (result, name) {
                    //$scope.reload();
                    file.name = name;
                });
            };


            $scope.showAuthorizer = function (file) {

                file = file || $scope.selection[0];
                var options;

                //if (FileSystem.isPrivate(file)) {
                options = {
                    templateUrl: 'asset/tpl/app/authorize.html',
                    controller: 'AuthorizeCtrl',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        id: function () {
                            return file.id;
                        }
                    }
                };
                var modalInstance = $modal.open(options);
                modalInstance.result.then(function (result) {
                    $scope.reload();
                });
            };

            $scope.move = function () {
                $rootScope.move($scope.selection, $scope.mount, function (result) {
                    $scope.reload();
                });
            };

            /**
             * 拷贝
             */
            $scope.copy = function () {
                $rootScope.copy($scope.selection, $scope.mount, function (result) {
                    $scope.reload();
                });
            };

            /**
             * 修改备注
             */
            $scope.updateDesc = function () {
                $rootScope.updateDescription($scope.current, function () {
                }, function () {
                });
            }

            $scope.createDirectory = function () {

                Messager.prompt("新建文件夹", "", "").then(function (name) {
                    var toaster = $scope.toaster.wait({
                        title: "正在新建文件夹"
                    });
                    var params = {
                        mount: $rootScope.mount.id,
                        name: name
                    };
                    if ($scope.current) {
                        params.parent = $scope.current.id;
                    }
                    FileSystem.createDirectory(params).then(
                        function () {
                            toaster.doSuccess({
                                type: "success",
                                title: "新建文件夹成功"
                            });
                            $scope.reload();
                        }, function (cause) {
                            toaster.doError({
                                type: "error",
                                title: "新建文件夹失败：" + cause
                            });
                        });
                });
            };

            $scope.showContextMenu = function (file, event) {
                var menu = angular.element("#directoryOperationContextMenu"), document = angular.element(window.document);
                $scope.selected = file;
                $scope.select(file, true, event);
                var top = event.pageY;
                var left = event.pageX;
                if (top + menu.height() > document.height()) {
                    top = top - menu.height();
                }

                if (left + menu.width() > document.width()) {
                    left = left - menu.width();
                }
                menu.css({
                    top: top,
                    left: left
                });
                menu.show();
                event.stopPropagation();
            };

            $scope.showDirectoryContextMenu = function (file, event) {
                var menu = angular.element("#directoryOperationContextMenu"), document = angular.element(window.document);
                $scope.selected = file;
                var top = event.pageY;
                var left = event.pageX;
                if (top + menu.height() > document.height()) {
                    top = top - menu.height();
                }

                if (left + menu.width() > document.width()) {
                    left = left - menu.width();
                }
                menu.css({
                    top: top,
                    left: left
                });
                menu.show();
                event.stopPropagation();
            }

            $scope.showPermissionContextMenu = function (authorize, event) {
                var menu = angular.element("#permissionContextMenu"), document = angular.element(window.document);
                $scope.selectedAuthorize = authorize;
                var top = event.pageY;
                var left = event.pageX;
                if (top + menu.height() > document.height()) {
                    top = top - menu.height();
                }

                if (left + menu.width() > document.width()) {
                    left = left - menu.width();
                }
                menu.css({
                    top: top,
                    left: left
                });
                menu.show();
                event.stopPropagation();
            };


            $scope.showContainerContextMenu = function (event) {
                var menu = angular.element("#containerContextMenu"), document = angular.element(window.document);
                var top = event.pageY;
                var left = event.pageX;
                if (top + menu.height() > document.height()) {
                    top = top - menu.height();
                }

                if (left + menu.width() > document.width()) {
                    left = left - menu.width();
                }
                menu.css({
                    top: top,
                    left: left
                });
                menu.show();
                event.stopPropagation();
            };


            $scope.reset = function () {
                $scope.selection = [];
                $scope.isSelectAll = false;
                $scope.lockedSelectionCount = false;
                // $rootScope.current = null;
            };

            function hideMenu(event) {
                angular.element("#directoryOperationContextMenu").hide();
                angular.element("#containerContextMenu").hide();
                angular.element("#permissionContextMenu").hide();
            }

            function handleF5(event) {
                var keycode = event.keyCode;
                if (keycode === 116) {
                    event.preventDefault();
                    $scope.reload();
                }
            }

            $(document).on("keydown", handleF5);
            $(window).on("click", hideMenu);
            $scope.$on("$destroy", function () {
                $(window).unbind("click", hideMenu);
                $(document).unbind("keydown", handleF5);
                $rootScope.setCurrent(null);
            });
        }]);

app.controller('DirectoryFormCtrl', [
    '$rootScope',
    '$scope',
    '$modalInstance',
    function ($rootScope, $scope, $modalInstance) {

        $scope.ok = function () {
            var result = {
                name: $scope.name,
                type: $scope.type
            };
            $modalInstance.close(result);
        };

        $scope.allowPrivate = function () {
            return ($rootScope.mountAlias === 'enterprise' && $scope.isAdmin() && !$rootScope.current);
        };

        $scope.close = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);
