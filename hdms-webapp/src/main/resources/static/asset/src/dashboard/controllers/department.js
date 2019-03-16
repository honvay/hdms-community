dashboard.controller('OrganizationCtrl', [
    '$scope',
    '$http',
    'Messager',
    '$modal',
    function ($scope, $http, Messager, $modal) {

        $scope.tree = {};

        $scope.status = {};

        $scope.onSelectionChange = function (selection) {
            $scope.current = selection;
            $scope.loadUser();
        }

        $scope.onTreeInit = function () {
            var root = $scope.tree.getRoot();
            $scope.tree.select(root.children[0]);
        }

        $scope.onTreeOptionClick = function (node, event) {
            event.stopPropagation();
            var menu = angular.element("#departmentMenu");
            menu.css({
                top: event.pageY,
                left: event.pageX
            })
            menu.show();
            $scope.selected = node;
        }

        function hideMenu(event) {
            //var menu = $(event.owner).closest(".dropdown-menu");
            var contextmenu = angular.element("#departmentMenu");
            //if(menu.length == 0){
            contextmenu.hide();
            //delete $scope.selected;
            //}else if(menu[0] != contextmenu[0]){
            //contextmenu.hide();
            //}
        }

        $(window).on("click", function (event) {
            hideMenu(event);
        });

        $scope.$on("$destroy", function () {
            $(window).unbind("click", hideMenu);
        });


        $scope.addDepartment = function () {
            var selected = $scope.selected;
            $scope.open({
                tpl: 'asset/tpl/dashboard/organization/department_form.html',
                ctrl: 'DepartmentCtrl',
                data: {
                    parent: selected
                },
                success: function () {
                    $scope.tree.reload(selected);
                }
            });
        }

        $scope.editDepartment = function () {
            var selected = $scope.selected;
            $scope.open({
                tpl: 'asset/tpl/dashboard/organization/department_form.html',
                ctrl: 'DepartmentCtrl',
                data: {
                    id: selected.id
                },
                success: function (result) {
                    if (result.oldParent && result.newParent) {
                        if (result.oldParent.id !== result.newParent) {
                            var oldNode = $scope.tree.get(result.oldParent.id);
                            $scope.tree.reload(oldNode);
                            var newNode = $scope.tree.get(result.newParent.id);
                            $scope.tree.reload(newNode);
                        } else if (result.newParent && !result.oldParent) {
                            var newNode = $scope.tree.get(result.newParent.id);
                            $scope.tree.reload(newNode);
                        } else {
                            var node = $scope.tree.get(result.department.id);
                            node.name = result.department.name;
                        }
                    } else {
                        var node = $scope.tree.get(result.department.id);
                        node.name = result.department.name;
                    }
                }
            });
        }


        $scope.deleteDepartment = function () {
            var selected = $scope.selected;
            var url = "/department/delete";
            var toast = $scope.toaster.wait('正在删除...');
            Messager.confirm("提示", "确定要删除?").then(
                function (result) {
                    $http.post(url, {
                        id: selected.id
                    }).success(
                        function (result, status, headers, config) {
                            if (result.success) {
                                toast.doSuccess('删除成功');
                                $scope.tree.remove(selected);
                            } else {
                                toast.doError('刪除失败：' + result.message);
                            }
                        }).error(function (result) {
                        toast.doError('刪除失败，稍候再试');
                    });
                });
        }


        $scope.loadUser = function () {
            $scope.status.loading = true;
            $scope.$broadcast("beforeLoadUsers");
            $scope.users = null;
            $http.get("/user/findByDepartment", {
                params: {
                    departmentId: $scope.current.id
                }
            }).success(
                function (result, status, headers, config) {
                    $scope.status.loading = false;
                    if (result.success) {
                        $scope.users = result.data;
                    } else {
                        $scope.status.error = "获取用户列表失败：" + result.message;
                    }
                }).error(function (data, status) {
                $scope.status.loading = false;
                $scope.status.error = "获取用户列表失败：" + (data.message || status);
            });
        }

        $scope.open = function (config) {
            var modalInstance = $modal.open({
                templateUrl: config.tpl,
                controller: config.ctrl,
                backdrop: 'static',
                resolve: {
                    data: function () {
                        return config.data;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (angular.isFunction(config.success)) {
                    config.success(result);
                }
                //load();
            }, function () {

            });
        }
    }]);


dashboard.controller('DepartmentCtrl', [
    '$scope',
    '$http',
    'data',
    '$modalInstance',
    'Messager',
    function ($scope, $http, data, $modalInstance, Messager) {
        $scope.showResult = false;
        $scope.operation = '添加';
        $scope.department = {};
        if (data.id != null) {
            $http.get("/department/get/" + data.id).success(function (result) {
                $scope.department = result.data;
                $scope.oldParent = $scope.department.parent;
            });
            $scope.operation = '编辑';
        } else {
            $scope.department = {
                parent: {
                    id: data.parent.id,
                    name: data.parent.name
                }
            };
        }

        $scope.onSelect = function (selection) {
            var parent = $scope.department.parent;
            if (parent && parent.id === selection.id) {
                return;
            }
            $scope.department.parent = {
                id: selection.id,
                name: selection.name,
                code: selection.code
            };
            $scope.departmentForm.$setDirty();
        }

        $scope.save = function () {
            var url = '/department/save';
            if ($scope.department.id) {
                url = '/department/update';
            }
            var toast = $scope.toaster.wait('正在保存...');
            $http.post(url, $scope.department).success(function (result) {
                if (result.success) {
                    toast.doSuccess("保存成功")
                    $modalInstance.close({
                        department: $scope.department,
                        oldParent: $scope.oldParent,
                        newParent:
                        $scope.department.parent
                    })
                } else {
                    $scope.result = result;
                }
            }).error(function (data, status, headers, config, statusText) {
                $scope.result = {
                    success: false,
                    message: "保存失败:" + status
                }
            });
        }

        $scope.cancel = function () {
            if ($scope.departmentForm.$dirty) {
                Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
                    function (result) {
                        $modalInstance.dismiss('cancel');
                    }, function () {
                    });
            } else {
                $modalInstance.dismiss('cancel');
            }
        }
    }]);
