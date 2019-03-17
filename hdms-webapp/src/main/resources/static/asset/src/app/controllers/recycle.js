app.controller('RecycleCtrl',
    ['$rootScope', '$scope', '$http', '$state', '$location', '$modal', 'Messager',
        function ($rootScope, $scope, $http, $state, $location,
                  $modal, Messager) {

            $rootScope.module = 'recycle';

            /** *************** 加载文件 ****************** */
            $scope.load = function () {
                $scope.reset();
                $scope.loading = true;
                $http.get('/recycle/list').success(function (result) {
                    $scope.loading = false;
                    if (result.success) {
                        $scope.files = result.data;
                    } else {
                        $scope.error = result.message;
                    }
                }).error(function (cause, status) {
                    $scope.error = (cause.message || status);
                    $scope.toaster.pop("error", (cause.message || status));
                    $scope.loading = false;
                });
            };

            $scope.$on("$viewContentLoaded", function () {
                $scope.load();
            });

            $scope.sortField = 'name';
            $scope.sortFields = ['type', 'name'];
            $scope.sortDesc = false;
            // 更改排序字段和规则
            $scope.changeSort = function (field) {
                if ($scope.sortField === field) {
                    $scope.sortDesc = !$scope.sortDesc;
                } else {
                    $scope.sortField = field;
                    $scope.sortDesc = true;
                }
                $scope.sortFields = ['type', field];
            };

            // 更改排序规则
            $scope.changeDesc = function () {
                $scope.sortDesc = !$scope.sortDesc;
            };

            $scope.revert = function () {
                Messager.confirm("提示", "确定要恢复?").then(function () {
                    var toast = $rootScope.toaster.wait("正在恢复...");
                    var id = Util.Array.copyProperty($scope.selection, 'id');
                    $http.post('/recycle/revert', {
                        documentIds: id
                    }).success(function (result) {
                        if (result.success) {
                            toast.doSuccess("文件已恢复");
                            $scope.load();
                        } else {
                            toast.doError("恢复失败:" + result.message);
                        }
                    }).error(function (data, status) {
                        toast.doError("恢复失败:" + (data.message || status));
                    });
                });
            };

            $scope.destroy = function () {
                Messager.confirm("提示", "确定要彻底删除?").then(function () {
                    var id = Util.Array.copyProperty($scope.selection, 'id');
                    var toast = $rootScope.toaster.wait("正在删除...");
                    $http.post('/recycle/delete', {
                        documentIds: id
                    }).success(function (result) {
                        if (result.success) {
                            toast.doSuccess("文件已彻底删除");
                            $scope.load();
                        } else {
                            toast.doError("删除失败:" + result.message);
                        }
                    }).error(function (data, status) {
                        toast.doError("删除失败:" + (data.message || status));
                    });
                });
            };

            $scope.clear = function () {
                Messager.confirm("提示", "确定要清空回收站?").then(function () {
                    var toast = $rootScope.toaster.wait("正在清空...");
                    $http.post('/recycle/clear').success(function (result) {
                        if (result.success) {
                            toast.doSuccess("回收站已清空");
                            $scope.load();
                        } else {
                            toast.doError("清空回收站失败:" + result.message);
                        }
                    }).error(function (data, status) {
                        toast.doError("清空回收站失败:" + (data.message || status));
                    });
                });
            };

            /** ********* 文件选择 *************** */
            $scope.isSelectAll = false;
            $scope.selection = [];

            $scope.selectAll = function () {
                $scope.isSelectAll = !$scope.isSelectAll;
                var len = $scope.files.length;
                for (var i = 0; i < len; i++) {
                    $scope.select($scope.files[i], $scope.isSelectAll);
                }
            };

            $scope.addSelection = function (file) {
                $scope.selection.push(file);
                file.selected = true;
                if ($scope.selection.length === $scope.files.length) {
                    $scope.isSelectAll = true;
                }
            };

            $scope.removeSelection = function (file) {
                $scope.selection.splice($scope.selection.indexOf(file), 1);
                file.selected = false;
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
                selected = selected || (file.selected ? !file.selected : true);
                selected === false ? $scope.removeSelection(file) : $scope.addSelection(file);
            };

            $scope.onContainerClick = function () {
                if ($scope.mode === 'block') {
                    $scope.clearSelection();
                }
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

            $scope.reset = function () {
                $scope.selection = [];
                $scope.isSelectAll = false;
                // $rootScope.current = null;
            };

            function handleF5(event) {
                var keycode = event.keyCode;
                if (keycode === 116) {
                    event.preventDefault();
                    $scope.load();
                }
            }

            $(document).keydown(handleF5);
            $scope.$on("$destroy", function () {
                $(document).unbind("keydown", handleF5);
            });
        }]);
