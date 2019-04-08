app.controller('FileDetailCtrl', ['$rootScope', '$scope', '$http', 'FileSystem', 'Messager', '$location', '$modal',
    '$modalInstance', 'id',
    function ($rootScope, $scope, $http, FileSystem, Messager, $location, $modal, $modalInstance, id) {

        var el = document.body;
        var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen;
        $scope.supportFullScreen = rfs;
        $scope.fullScreen = function () {
            var container = document.getElementById("file-preview-container");
            var rfs = container.requestFullScreen || container.webkitRequestFullScreen || container.mozRequestFullScreen || container.msRequestFullScreen;
            rfs.call(container);
            $scope.hiddenDetail = true;
        };

        $scope.deg = 0;
        $scope.rotate = function () {
            $scope.deg += 90;
            if ($scope.deg === 360) {
                $scope.deg = 0;
            }
        };
        //添加评论
        $scope.addReview = function (content) {
            var toast = $scope.toaster.wait("正在发表评论");
            $http.post('/review/add', {
                content: content,
                documentId: $scope.file.id
            }).success(function (result) {
                if (result.success) {
                    toast.doSuccess("评论发表成功");
                    var review = result.data;
                    review.name = $scope.user.name;
                    review.avatar = $scope.user.avatar;
                    $scope.reviews.push(review);
                } else {
                    toast.doError("发表评论失败: " + result.message);
                }
            }).error(function (result, status) {
                toast.doError("发表评论失败: " + (result.message || status));
            });
        }

        $scope.removeReview = function (review) {
            Messager.confirm("提示", "确定要删除评论?").then(function () {
                var toast = $scope.toaster.wait("正在删除评论");
                $http.post('/review/delete', {
                    id: review.id
                }).success(function (result) {
                    if (result.success) {
                        toast.doSuccess("评论已删除");
                        $scope.reviews.splice($scope.reviews.indexOf(review), 1);
                    } else {
                        toast.doError("删除评论失败: " + result.message);
                    }
                }).error(function (result, status) {
                    toast.doError("删除评论失败: " + (result.message || status));
                });
            });
        }

        $scope.addFavorite = function () {
            FileSystem.addFavorite($scope.file).then(function () {
                $rootScope.toaster.success({
                    title: '已收藏'
                });
                $scope.favorited = true;
            }, function () {
                $rootScope.toaster.error({
                    title: '收藏失败'
                });
            });
        };

        $scope.removeFavorite = function () {
            FileSystem.removeFavorite($scope.file).then(function () {
                $rootScope.toaster.success({
                    title: '收藏已取消'
                });
                $scope.favorited = false;
            }, function () {
                $rootScope.toaster.error({
                    title: '取消收藏失败'
                });
            });
        };

        // 删除文件
        $scope.remove = function () {
            $rootScope.remove($scope.file, function () {
                $scope.close(true);
            });
        };

        $scope.rename = function () {
            $rootScope.rename($scope.file, function (result, name) {
                $scope.file.name = name;
            });
        };

        $scope.move = function () {
            $rootScope.move([$scope.file], $scope.file.mount, function (result) {
                $scope.close(true);
            });
        };

        $scope.lock = function () {
            $rootScope.lock($scope.file, function (result) {
                $scope.load(id)
            });
        };

        $scope.unlock = function () {
            $rootScope.unlock($scope.file, function (result) {
                $scope.load(id)
            });
        };

        $scope.copy = function () {
            $rootScope.copy([$scope.file]);
        };

        $scope.download = function (file) {
            if (!file) {
                file = $scope.file;
            }
            $rootScope.download(file);
        };

        $scope.load = function (id) {
            $scope.loading = true;
            $http.get("/fs/document/" + id).success(function (result) {
                $scope.loading = false;
                if (result.success) {
                    $scope.file = result.data.document;
                    $scope.favorited = result.data.document.favoriteId != null;
                    $scope.permissions = result.data.permissions;
                    $scope.activities = result.data.activities;
                    $scope.reviews = result.data.reviews;
                    $scope.token = result.data.token;
                    $scope.setCurrent($scope.file);
                    if (!$rootScope.mount) {
                        $rootScope.mountAlias = $rootScope.mountAliases[$scope.file.mount];
                        $rootScope.mount = $rootScope.mounts[$rootScope.mountAlias];
                    }
                } else {
                    $scope.error = result.message || '加载失败';
                }
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.error = status;
            });
        };

        $scope.setVersion = function (file) {
            Messager.confirm("提示", "确定将该版本设置为最新版本?").then(function () {
                var toast = $rootScope.toaster.wait("正在设置新版本...");
                $http.post("/fs/version/set", {
                    id: file.id
                }).success(function (result) {
                    if (result.success) {
                        toast.doSuccess("设置最新版本成功");
                        $scope.load(result.file.id);
                    } else {
                        toast.doError("设置最新版本失败:" + result.message);
                    }
                }).error(function (data, status) {
                    toast.doError("设置最新版本失败:" + (data.message || status));
                });
            });
        };

        $scope.deleteVersion = function (file) {
            Messager.confirm("提示", "版本删除之后不可恢复，确定删除该版本?").then(function () {
                var toast = $rootScope.toaster.wait("正在删除...");
                $http.post("/fs/version/delete/" + file.id).success(function (result) {
                    if (result.success) {
                        toast.doSuccess("删除版本成功");
                        $scope.load($scope.file.id);
                    } else {
                        toast.doError("删除版本失败:" + result.message);
                    }
                }).error(function (data, status) {
                    toast.doError("删除版本失败:" + (data.message || status));
                });
            });
        };

        $scope.setCurrent = function (file) {
            $scope.current = file;
            $scope.current.media = $scope.getMediaType($scope.current);
            $scope.current.icon = $rootScope.getIcon($scope.current);
            if ($scope.current.media === 'code') {
                $scope.cmOption = {
                    lineNumbers: true,
                    indentWithTabs: true,
                    mode: $scope.getExt(file),
                    readOnly: true,
                    onLoad: function (_cm) {
                        _cm.setOption("css", "height:100%");
                    }
                }
                $http.get('/fs/raw?token=' + $scope.token).success(function (result) {
                    $scope.raw = result;
                });
            }
        };

        $scope.checkPermission = function (permission) {
            // 可以在我的文档中任意下载
            if ($scope.mountAlias === 'my') {
                return true;
            }

            // 如果不根目录则用当前文件夹的权限来匹配
            if ($scope.permissions && $scope.permissions.length > 0) {
                return $scope.permissions.indexOf(permission) >= 0;
            }
            return true;
        };

        $scope.getMediaType = function (file) {
            var ext = $scope.getExt(file);
            if (!ext) {
                return;
            }
            var media = $scope.mediaType[ext.toLowerCase()];
            if (!media) {
                return 'unknow';
            }
            return media.type;
        };

        $scope.geImageSrc = function (file) {
            return "/fs/raw?token=" + $scope.token;
        };

        $scope.getPdfViewerUrl = function (file) {
            return 'asset/pdfjs/viewer.html?token=' + $scope.token;
        }

        $scope.getPreviewUrl = function (file) {
            return "/fs/office?id=" + $scope.current.id;
        };

        $scope.close = function (reload) {
            $modalInstance.close(reload);
            var path = "/index/";
            if ($rootScope.module) {
                switch ($rootScope.module) {
                    case 'search' :
                        path += "search/" + $rootScope.keyword;
                        break;
                    case 'directory':
                        path += "directory/" + ($scope.file.parent !== -1 ? $scope.file.parent : $rootScope.getMountAlias($scope.file.mount));
                        break;
                    case undefined:
                        path += "directory/" + ($scope.file.parent !== -1 ? $scope.file.parent : $rootScope.getMountAlias($scope.file.mount));
                        break;
                    default:
                        path += $rootScope.module;
                }
            } else {
                path += "directory/enterprise";
            }
            /*if ($rootScope.module) {
                path += $rootScope.module;
                //重新设置路径
                if ($rootScope.module === 'search') {

                } else {
                    path += "/" + ($scope.file.parent !== -1 ? $scope.file.parent : $rootScope.getMountAlias($scope.file.mountType));
                }
            } else {
            }*/

            $location.path(path);
        };

        $scope.onContainerClick = function () {
            angular.element("#documentOperationContextMenu").hide();
        }

        $scope.load(id);

        $scope.showDocumentContextMenu = function (file, event) {
            var menu = angular.element("#documentOperationContextMenu"), document = angular.element(window.document);
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
    }]);

app.controller('FileMoveCtrl', [
    '$rootScope',
    '$scope',
    'Messager',
    '$modalInstance',
    'data',
    function ($rootScope, $scope, Messager, $modalInstance, data) {

        var excludes = Util.Array.copyProperty(data.files, 'id',
            function (file) {
                return file.type === 'directory';
            });

        $scope.isRepeat = false;

        $scope.ok = function () {
            Messager.confirm("提示", "是否确定要移动?").then(function () {
                $modalInstance.close($scope.target);
            });
        };

        $scope.setParam = function (node, params) {
            params.mount = node.mount;
            if (!params.excludes) {
                //params.excludes = excludes;
            }
        };

        $scope.setTarget = function (node) {
            var id = data.files[0].parent;
            var mount = data.files[0].mount;
            if (id && id === node.id) {
                $scope.isRepeat = true;
            } else if (!id && !node.id && mount === node.mount) {
                $scope.isRepeat = true;
            } else {
                $scope.isRepeat = false;
            }

            if ($scope.isRepeat === true) {
                $scope.target = null;
                $scope.paths = null;
                return;
            }

            var paths = [];
            $scope.target = node;
            paths.push(node.name);
            while (node.$parent && !node.$parent.$root) {
                node = node.$parent;
                paths.push(node.name);
            }
            paths.reverse();
            $scope.paths = paths.join(" / ");
        };

        /**
         * 修改备注
         */
        $scope.updateDesc = function () {
            $rootScope.updateDescription($scope.current, function () {
            }, function () {
            });
        }

        $scope.close = function () {
            $modalInstance.dismiss();
        };

    }]);

app.controller('FileCopyCtrl', [
    '$rootScope',
    '$scope',
    'Messager',
    '$modalInstance',
    'data',
    function ($rootScope, $scope, Messager, $modalInstance, data) {
        $scope.ok = function () {
            Messager.confirm("提示", "是否确定要复制?").then(function () {
                $modalInstance.close($scope.target);
            });
        }

        $scope.setParam = function (node, params) {
            params.mount = node.mount;
        }

        $scope.setTarget = function (node) {
            var paths = [];
            $scope.target = node;
            paths.push(node.name);
            while (node.$parent && !node.$parent.$root) {
                node = node.$parent;
                paths.push(node.name);
            }
            paths.reverse();
            $scope.paths = paths.join(" / ");
        }

        $scope.close = function () {
            $modalInstance.dismiss();
        }
    }]);
app.controller('FileTagCtrl', [
    '$scope',
    '$http',
    '$modalInstance',
    'Messager',
    'FileSystem',
    'file',
    function ($scope, $http, $modalInstance, Messager, FileSystem, file) {
        $scope.tags = [];
        $scope.file = file;
        if (file.tags) {
            $scope.tags = file.tags.split(",");
        }
        $scope.remove = function (tag) {
            $scope.tags.splice($scope.tags.indexOf(tag), 1);
            file.tags = $scope.tags.join(",");
            $scope.save();
        }

        $scope.add = function () {
            if ($scope.tags.indexOf($scope.tag) >= 0) {
                $scope.toaster.pop("error", "存在相同的标签");
                return;
            }
            $scope.tags.push($scope.tag);
            $scope.tag = null;
            file.tags = $scope.tags.join(",");
            $scope.save();
        }

        $scope.save = function () {
            FileSystem.setTag($scope.file).then(function (result) {

            });
        }

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        }
    }]);

app.controller('FileSendCtrl', [
    '$scope',
    '$http',
    'files',
    '$modal',
    '$modalInstance',
    'Messager',
    function ($scope, $http, files, $modal, $modalInstance, Messager) {
        $scope.files = files;
        $scope.remove = function (file) {
            $scope.files.splice($scope.files.indexOf(file), 1);
            if ($scope.files.length === 0) {
                $modalInstance.dismiss('cancel');
            }
        }

        $scope.users = [];
        $scope.userIds = {};
        $scope.addReceiver = function () {
            var modalInstance = $modal.open({
                templateUrl: 'asset/tpl/app/user_select.html',
                controller: 'SelectUserCtrl',
                backdrop: 'static',
                resolve: {
                    options: function () {
                        return {
                            multiple: true
                        };
                    }
                }
            });
            modalInstance.result.then(function (users) {
                users.forEach(function (user) {
                    if (!$scope.userIds[user.id]) {
                        $scope.users.push(user);
                        $scope.userIds[user.id] = user;
                    }
                });
            });
        }

        $scope.removeReceiver = function (user) {
            $scope.users.splice($scope.users.indexOf(user), 1);
            delete $scope.userIds[user.id];
        }

        $scope.ok = function () {
            var toast = $scope.toaster.wait("正在发送");
            $scope.sending = true;
            var files = Util.Array.copyProperty($scope.files, "id");
            var receivers = Util.Array.copyProperty($scope.users, "id");
            $http.post("/message/send", {
                files: files,
                receivers: receivers,
                content: $scope.content
            }).success(function (result) {
                $scope.sending = false;
                if (result.success) {
                    toast.doSuccess("发送成功");
                    $modalInstance.close();
                } else {
                    toast.doError("发送失败:" + result.message);
                }
            }).error(function (result, status) {
                toast.doError("发送失败:" + (result.message || status));
            });
        }

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        }
    }]);