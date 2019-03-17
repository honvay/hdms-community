app.controller('AuthorizePublicCtrl', [
    '$rootScope',
    '$scope',
    '$http',
    '$modal',
    '$modalInstance',
    'data',
    function ($rootScope, $scope, $http, $modal, $modalInstance, data) {

        $scope.permission = data.permission;

        var toast = $rootScope.toaster.wait("正在加载权限...")
        $http.get('/conf/permission').success(function (result) {
            if (result.success) {
                $scope.permissions = result.permissions;
                toast.doSuccess("权限已加载");
            } else {
                toast.doError("加载权限列表失败:" + result.message);
            }
        }).error(function (result, status) {
            toast.doError("加载权限列表失败:" + (result.mesage || status));
        });


        $scope.ok = function () {
            var params = {
                permission: $scope.permission,
                id: data.id
            }
            $scope.saving = true;
            var toast = $rootScope.toaster.wait("正在设置权限")
            $http.post('/fs/permission/set', params).success(function (result) {
                $scope.saving = false;
                if (result.success) {
                    toast.doSuccess('权限已设置');
                    $modalInstance.close();
                } else {
                    toast.doError("权限设置失败:" + result.message);
                }
            }).error(function (data, status) {
                $scope.saving = false;
                toast.doError("权限设置失败:" + (data.message || status));
            });
        }
        $scope.close = function () {
            $modalInstance.dismiss('cancel');
        }
    }]);


app.controller('AuthorizeCtrl', [
    '$rootScope',
    '$scope',
    '$http',
    'Messager',
    '$modal',
    '$modalInstance',
    'id',
    function ($rootScope, $scope, $http, Messager, $modal, $modalInstance, id) {

        $scope.permissionMap = {};
        for (var i = 0; i < $scope.permissions.length; i++) {
            $scope.permissionMap[$scope.permissions[i].id] = $scope.permissions[i].name;
        }
        $scope.select = function (authorize, event) {
            event.stopPropagation();
            if ($scope.current === authorize) {
                $scope.current = null;
                return;
            }
            $scope.current = authorize;
        }

        $scope.condition = {};

        $scope.changeConditionPermission = function (permission) {
            $scope.condition.permission = permission;
        }

        $scope.load = function () {
            $scope.loading = true;
            $http.get("/fs/authorize/list", {
                params: {
                    documentId: id
                }
            }).success(function (result, status, headers, config) {
                $scope.loading = false;
                if (result.success) {
                    $scope.authorizes = result.data;
                } else {
                    $scope.toaster.pop('error', result.message);
                    $scope.error = result.message;
                }
            }).error(function (result, status) {
                $scope.loading = false;
                $scope.error = result.message;
                $scope.toaster.pop('error', '加载失败：' + status);
            });
        }

        $scope.count = function (type) {
            if (!$scope.authorize || $scope.authorize.length === 0) return false;
            var count = 0;
            $scope.authorize.forEach(function (item) {
                if (item.type === type) {
                    count++;
                }
            });
            return count > 0;
        }

        $scope.add = function () {
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
                        return id;
                    }
                }
            });

            modalInstance.result.then(function (users) {
                $scope.load();
            }, function () {

            });
        }


        $scope.update = function (authorize) {
            if ($scope.permissionId !== authorize.permissionId) {
                Messager.confirm("提示", "确定要修改权限?").then(function () {
                    doUpdate();
                });
            }

            function doUpdate() {
                var toast = $scope.toaster.wait('正在更新权限...');
                if (authorize.inherited === true) {
                    var params = {
                        documentId : id,
                        permission : $scope.permissionId
                    }

                    if($scope.current.ownerType === 1){
                        params.userIds = [$scope.current.owner];
                    }else{
                        params.departmentIds = [$scope.current.owner];
                    }

                    $http.post('/fs/authorize/add', params)
                        .success(function (result) {
                            if (result.success) {
                                toast.doSuccess('权限已更新');
                                $scope.load();
                            } else {
                                toast.doError('权限更新失败:' + result.message);
                            }
                        }).error(function (result, status) {
                        toast.doError('权限更新失败:' + (result.message || status));
                    });
                }else{
                    $http.post('/fs/authorize/update', {
                        id: authorize.id,
                        permissionId: $scope.permissionId
                    }).success(function (result) {
                        if (result.success) {
                            $scope.load();
                            toast.doSuccess('权限已更新');
                        } else {
                            toast.doError('权限更新失败:' + result.message);
                        }
                    }).error(function (result, status) {
                        toast.doError('权限更新失败:' + (result.message || status));
                    });
                }

            }
        }

        $scope.remove = function (authorize, event) {
            event.stopPropagation();
            Messager.confirm("提示", "确定要删除?").then(function () {
                doRemove(authorize);
            });

            function doRemove(authorize) {
                var toast = $scope.toaster.wait('正在删除权限...');
                var http = $http.post('/fs/authorize/remove/' + authorize.id, {});
                http.success(function (result) {
                    if (result.success) {
                        $scope.load();
                        $scope.current = null;
                        toast.doSuccess('权限已删除');
                    } else {
                        toast.doError('删除权限失败:' + result.message);
                    }
                }).error(function (data, status) {
                    toast.doError('权限删除失败:' + (result.message || status));
                });
            }
        }

        $scope.ok = function () {
            /*var result = {
                name : $scope.name,
                type : $scope.type
            };*/
            $modalInstance.close(result);
        }

        $scope.close = function () {
            $modalInstance.dismiss('cancel');
        }

        $scope.load()
    }]);

app.controller('AuthorizeAddCtrl', [
    '$rootScope',
    '$scope',
    '$http',
    '$modal',
    '$modalInstance',
    'permissions',
    'id',
    function ($rootScope, $scope, $http, $modal, $modalInstance, permissions, id) {
        $scope.loading = false;
        $scope.tree = {};
        $scope.permissions = permissions;
        $scope.onTreeInit = function () {
            var root = $scope.tree.getRoot();
            $scope.tree.select(root);
        }

        $scope.clear = function () {
            $scope.searching = false;
            $scope.condition = null;
        }

        $scope.selectedUsers = [];
        $scope.selectedUserIds = [];
        $scope.selectedDepartments = [];
        $scope.selectedDepartmentIds = [];
        $scope.addDepartment = function (department) {
            if ($scope.selectedDepartmentIds.indexOf(department.id) >= 0) {
                return;
            }
            $scope.selectedDepartments.push(department);
            $scope.selectedDepartmentIds.push(department.id);
        }

        $scope.removeDepartment = function (department) {
            if ($scope.selectedDepartmentIds.indexOf(department.id) < 0) {
                return;
            }
            $scope.selectedDepartments.splice($scope.selectedDepartments.indexOf(department), 1);
            $scope.selectedDepartmentIds.splice($scope.selectedDepartmentIds.indexOf(department.id), 1);
        }

        $scope.addUser = function (user) {
            if ($scope.selectedUserIds.indexOf(user.id) >= 0) {
                return;
            }
            $scope.selectedUsers.push(user);
            $scope.selectedUserIds.push(user.id);
        }

        $scope.removeUser = function (user) {
            if ($scope.selectedUserIds.indexOf(user.id) < 0) {
                return;
            }
            $scope.selectedUsers.splice($scope.selectedUsers.indexOf(user), 1);
            $scope.selectedUserIds.splice($scope.selectedUserIds.indexOf(user.id), 1);
        }


        $scope.search = function () {
            if (!$scope.condition) {
                return;
            }
            $scope.searching = true;
            $scope.loading = true;
            $http.get("/member/search", {
                params: {
                    condition: $scope.condition
                }
            }).success(function (result, status, headers, config) {
                $scope.loading = false;
                if (result.success) {
                    $scope.searchResult = result.data;
                } else {
                    $scope.toaster.pop('error', '提示', result.message);
                }
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.toaster.pop('error', '提示', '加载失败：' + status);
            });
        }

        $scope.ok = function () {
            var params = {
                documentId: id,
                userIds: $scope.selectedUserIds,
                departmentIds: $scope.selectedDepartmentIds,
                permission: $scope.permission,
            }
            var toast = $scope.toaster.wait('正在添加权限...');
            $scope.saving = true;
            $http.post('/fs/authorize/add', params).success(function (result) {
                $scope.saving = false;
                if (result.success) {
                    toast.doSuccess("权限已添加");
                    $modalInstance.close();
                } else {
                    toast.doError("添加权限失败：" + result.message);
                    //$scope.toaster.error('提交失败：' + result.message);
                }
            }).error(function (result, status) {
                $scope.saving = false;
                toast.doError("添加权限失败：" + (result.message || status));
            });
        }

        $scope.loadMember = function (dept) {
            $scope.loading = true;
            $http.get("/member/list", {
                params: {
                    departmentId: dept.id
                }
            }).success(function (result, status, headers, config) {
                $scope.loading = false;
                if (result.success) {
                    $scope.users = result.data.users;
                    $scope.departments = result.data.departments;
                } else {
                    $scope.toaster.pop('error', result.message);
                }
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.toaster.pop('error', '加载失败：' + status);
            });
        }
        $scope.close = function () {
            $modalInstance.dismiss('cancel');
        }
    }]);