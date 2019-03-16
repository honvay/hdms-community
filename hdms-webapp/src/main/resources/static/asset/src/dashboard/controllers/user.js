dashboard.controller('UserCtrl', [
    '$scope',
    '$http',
    'Messager',
    '$modal',
    function ($scope, $http, Messager, $modal) {

        $scope.$on('beforeLoadUsers', function () {
            $scope.selected = null;
        });

        $scope.select = function (user) {
            if ($scope.selected && $scope.selected !== user) {
                $scope.selected.selected = false;
            }
            if (user.selected !== true) {
                user.selected = true;
                $scope.selected = user;
            } else {
                user.selected = false;
                $scope.selected = null;
            }
        }

        $scope.addUser = function () {
            $scope.open({
                tpl: 'asset/tpl/dashboard/organization/user_form.html',
                ctrl: 'UserFormCtrl',
                data: {
                    department: $scope.current
                },
                success: function () {
                    $scope.loadUser();
                }
            });
        }

        $scope.operation = function (type, user, event) {
            event.stopPropagation();
            user.executing = true;
            var url = "/user/" + type + "/" + user.id;
            var toast = $scope.toaster.wait("正在执行...");
            $http.post(url, {}).success(function (result) {
                user.executing = false;
                if (result.success) {
                    toast.doSuccess("操作成功");
                    user.status = result.data;
                } else {
                    toast.doError('操作失败：' + result.message);
                }
            }).error(function (data, status) {
                user.executing = false;
                toast.doError('操作失败：' + (data.message || status));
            });
        }

        $scope.reset = function (event) {
            Messager.confirm("提示", "确定将该用户的密码重置为系统默认密码?").then(function () {
                var user = $scope.selected;
                user.executing = true;
                var url = '/user/reset/' + user.id;
                var toast = $scope.toaster.wait("正在重置密码...");
                $http.post(url, {}).success(function (result) {
                    user.executing = false;
                    if (result.success) {
                        toast.doSuccess("重置密码成功");
                    } else {
                        toast.doError("重置密码失败：" + result.message);
                    }
                }).error(function (data, status, headers, config, statusText) {
                    user.executing = false;
                    toast.doError("重置密码失败：" + (data.message || status));
                });
            });
        }

        $scope.disable = function (user, event) {
            Messager.confirm("提示", "确定禁用该用户?").then(function () {
                $scope.operation('disable', user, event);
            });
        }

        $scope.enable = function (user, event) {
            Messager.confirm("提示", "确定启用该用户?").then(function () {
                $scope.operation('enable', user, event);
            });
        }

        $scope.lock = function (user, event) {
            Messager.confirm("提示", "确定锁定该用户?").then(function () {
                $scope.operation('lock', user, event);
            });
        }
        $scope.unlock = function (user, event) {
            Messager.confirm("提示", "确定解锁该用户?").then(function () {
                $scope.operation('unlock', user, event);
            });
        }

        $scope.setQuota = function () {
            var selected = $scope.selected;
            $scope.open({
                tpl: 'asset/tpl/dashboard/organization/user_quota.html',
                ctrl: 'UserQuotaCtrl',
                data: {
                    user: $scope.selected
                },
                success: function (result) {
                    $scope.toaster.pop('success', '调整配额成功');
                    //$scope.loadUser();
                }
            });
        }

        $scope.transfer = function () {
            var selected = $scope.selected;
            $scope.open({
                tpl: 'asset/tpl/dashboard/organization/user_transfer.html',
                ctrl: 'UserTransferCtrl',
                data: {
                    user: $scope.selected
                },
                success: function (result) {
                    //$scope.toaster.pop('success','调整配额成功');
                    //$scope.loadUser();
                }
            });
        }

        $scope.transferAdmin = function (user) {
            if ($scope.selected.id === $scope.user.id) {
                Messager.alert("提示", "不能移交给自己");
                return;
            }
            Messager.confirm("提示", "管理员权限移交后将会自动退出。确定要移交管理员权限？").then(function () {
                if (!$scope.selected) return;
                var params = {
                    id: $scope.selected.id
                }
                var toast = $scope.toaster.wait('正在移交权限...');
                $scope.saving = true;
                $http.post('/user/admin/transfer', params).success(function (result) {
                    $scope.saving = false;
                    if (result.success) {
                        toast.doClose();
                        Messager.alert("提示", "权限移交成功").then(function () {
                            window.location.href = "/logout";
                        });
                    } else {
                        toast.doError("权限移交失败：" + result.message);
                        //$scope.toaster.error('提交失败：' + result.message);
                    }
                }).error(function (result, status) {
                    $scope.saving = false;
                    toast.doError("权限移交失败：" + (result.message || status));
                });
            });
        }


        $scope.editUser = function () {
            var selected = $scope.selected;
            $scope.open({
                tpl: 'asset/tpl/dashboard/organization/user_form.html',
                ctrl: 'UserFormCtrl',
                data: {
                    id: selected.id
                },
                success: function (department) {
                    $scope.loadUser();
                }
            });
        }
    }]);
//添加用户
dashboard.controller('UserFormCtrl', [
    '$scope',
    '$http',
    'data',
    '$modalInstance',
    'Messager',
    function ($scope, $http, data, $modalInstance, Messager) {
        $scope.operation = '添加';
        if (data.id != null) {
            $http.get("/user/get/" + data.id).success(function (result) {
                $scope.user = result.data;
                if (!$scope.user.department) {
                    delete $scope.user.department;
                }
            });
            $scope.operation = '编辑';
        } else {
            $scope.user = {
                departmentId: data.department.id,
                departmentName: data.department.name
            };
        }

        $scope.onSelect = function (selection) {
            var department = $scope.user.department;
            if (department && department.id === selection.id) {
                return;
            }
            $scope.user.departmentId = selection.id;
            $scope.user.departmentName = selection.name;
            $scope.userForm.$setDirty();
        }

        $scope.save = function () {
            $scope.saving = true;
            var url = '/user/save';
            if ($scope.user.id) {
                url = '/user/update';
            }
            var toast = $scope.toaster.wait('正在保存...');
            $http.post(url, $scope.user).success(function (result) {
                $scope.saving = false;
                if (result.success) {
                    toast.doSuccess("保存成功");
                    $modalInstance.close($scope.user);
                } else {
                    toast.doError("保存失败：" + result.message);
                    $scope.result = result;
                }
            }).error(function (data, status, headers, config, statusText) {
                toast.doError("保存失败：" + data.message);
                $scope.saving = false;
                $scope.result = {
                    success: false,
                    message: "保存失败:" + status
                }
            });
        }

        $scope.cancel = function () {
            if ($scope.userForm.$dirty) {
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
//设置用户配额
dashboard.controller('UserQuotaCtrl', [
    '$scope',
    '$http',
    'data',
    '$modalInstance',
    'Messager',
    function ($scope, $http, data, $modalInstance, Messager) {
        $scope.user = data.user;
        $scope.save = function () {
            var url = '/user/set/quota';
            $scope.saving = true;
            $http.post(url, {
                id: $scope.user.id,
                quota: $scope.user.quota
            }).success(function (result) {
                $scope.saving = false;
                if (result.success) {
                    $modalInstance.close();
                } else {
                    $scope.result = result;
                }
            }).error(function (data, status, headers, config, statusText) {
                $scope.saving = false;
                $scope.result = {
                    success: false,
                    message: "保存失败:" + (data.message || status)
                }
            });
        }

        $scope.cancel = function () {
            if ($scope.userQutaoForm.$dirty) {
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
//文档移交Controller
dashboard.controller('UserTransferCtrl', [
    '$rootScope',
    '$scope',
    '$http',
    '$modal',
    '$modalInstance',
    'data',
    'Messager',
    function ($rootScope, $scope, $http, $modal, $modalInstance, data, Messager) {
        $scope.loading = false;
        $scope.tree = {};
        $scope.user = data.user;
        $scope.onTreeInit = function () {
            var root = $scope.tree.getRoot();
            $scope.tree.select(root);
        }

        $scope.clear = function () {
            $scope.searching = false;
            $scope.condition = null;
        }

        $scope.select = function (user) {
            $scope.selected = user;
        }

        $scope.search = function () {
            if (!$scope.condition) {
                return;
            }
            $scope.searching = true;
            $scope.loading = true;
            $http.get("/user/search", {
                params: {
                    condition: $scope.condition
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (data.success) {
                    $scope.result = data.users;
                } else {
                    $scope.toaster.pop('error', '提示', data.message);
                }
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.toaster.pop('error', '提示', '加载失败：' + status);
            });
        }

        $scope.transfer = function (user) {
            $scope.select(user);
            if (!$scope.selected.id == $scope.user.id) {
                $scope.ok();
            }
        }

        $scope.ok = function () {

            Messager.confirm("提示", "文档移交后不可恢复，请慎重操作。确定要进行文档？").then(function () {
                if (!$scope.selected) return;
                var params = {
                    source: $scope.user.id,
                    target: $scope.selected.id
                }
                var toast = $scope.toaster.wait('正在移交文档...');
                $scope.saving = true;
                $http.post('/fs/transfer', params).success(function (result) {
                    $scope.saving = false;
                    if (result.success) {
                        toast.doSuccess("文档移交成功");
                        $modalInstance.close();
                    } else {
                        toast.doError("文档移交失败：" + result.message);
                        //$scope.toaster.error('提交失败：' + result.message);
                    }
                }).error(function (result, status) {
                    $scope.saving = false;
                    toast.doError("文档移交失败：" + (result.message || status));
                });
            });
        }

        $scope.loadUser = function (dept) {
            $scope.loading = true;
            $http.get("/user/list", {
                params: {
                    departmentId: dept.id
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (data.success) {
                    $scope.users = data.users;
                } else {
                    $scope.toaster.pop('error', data.message);
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

dashboard.controller('UserTransferAdminCtrl', [
    '$rootScope',
    '$scope',
    '$http',
    '$modal',
    '$modalInstance',
    'Messager',
    function ($rootScope, $scope, $http, $modal, $modalInstance, Messager) {
        $scope.loading = false;
        $scope.tree = {};
        $scope.onTreeInit = function () {
            var root = $scope.tree.getRoot();
            $scope.tree.select(root);
        }

        $scope.clear = function () {
            $scope.searching = false;
            $scope.condition = null;
        }

        $scope.select = function (user) {
            $scope.selected = user;
        }

        $scope.search = function () {
            if (!$scope.condition) {
                return;
            }
            $scope.searching = true;
            $scope.loading = true;
            $http.get("/user/search", {
                params: {
                    condition: $scope.condition
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (data.success) {
                    Messager.alert("提示", "您的管理权限已移交").then(function () {
                        window.location.href = "/login";
                    });
                    $scope.result = data.users;
                } else {
                    $scope.toaster.pop('error', '提示', data.message);
                }
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.toaster.pop('error', '提示', '加载失败：' + status);
            });
        }

        $scope.transfer = function (user) {
            $scope.select(user);
            if (!$scope.selected.id == $scope.user.id) {
                $scope.ok();
            }
        }

        $scope.ok = function () {

            Messager.confirm("提示", "管理员权限移交后将会自动退出。确定要移交管理员权限？").then(function () {
                if (!$scope.selected) return;
                var params = {
                    source: $scope.user.id,
                    target: $scope.selected.id
                }
                var toast = $scope.toaster.wait('正在移交权限...');
                $scope.saving = true;
                $http.post('/user/transfer/admin', params).success(function (result) {
                    $scope.saving = false;
                    if (result.success) {
                        toast.doSuccess("权限移交成功");
                        $modalInstance.close();
                    } else {
                        toast.doError("权限移交失败：" + result.message);
                        //$scope.toaster.error('提交失败：' + result.message);
                    }
                }).error(function (result, status) {
                    $scope.saving = false;
                    toast.doError("权限移交失败：" + (result.message || status));
                });
            });
        }

        $scope.loadUser = function (dept) {
            $scope.loading = true;
            $http.get("/user/list", {
                params: {
                    departmentId: dept.id
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (data.success) {
                    $scope.users = data.users;
                } else {
                    $scope.toaster.pop('error', data.message);
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