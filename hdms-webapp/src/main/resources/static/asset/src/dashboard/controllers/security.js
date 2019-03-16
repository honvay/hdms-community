'use strict';

dashboard.controller('LoginLogCtrl', ['$scope', '$http', '$state',
    function ($scope, $http, $state) {
        $scope.load = function () {
            $scope.loading = true;
            $scope.error = null;
            $scope.result = null;
            $http.get("/login/log/search", {
                params: $scope.buildParam()
            }).success(function (result) {
                $scope.loading = false;
                if (result.success === false) {
                    $scope.error = result.message;
                }
                $scope.result = result.data;
                $scope.pagination.pages = Math.ceil($scope.result.total / $scope.pagination.size);
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.error = data.message || status;
            });
        }

        $scope.reload = function () {
            $scope.pagination.page = 1;
            $scope.load();
        }

        $scope.loadPage = function (page) {
            $scope.pagination.page = page;
            $scope.load();
        }

        $scope.result = {
            rows: []
        }

        $scope.pagination = {
            size: 20,
            page: 1,
            pages: 0
        };

        $scope.onPageChange = function () {
            if ($scope.pagination.page > $scope.pagination.pages) return;
            $scope.reload();
        }

        $scope.condition = {
            start: null,
            end: null,
            username: null,
            name: null
        }

        $scope.buildParam = function () {
            var start = $scope.condition.start;
            var end = $scope.condition.end;
            var param = {
                size: $scope.pagination.size,
                page: $scope.pagination.page,
                startDate: start ? moment(start).format("YYYY-MM-DD") + " 00:00:00" : null,
                endDate: end ? moment(end).format("YYYY-MM-DD") + " 23:59:59" : null,
                username: $scope.condition.username,
                name: $scope.condition.name
            };
            return param;
        }

        $scope.load();

    }]);

dashboard.controller('ActivityCtrl', ['$scope', '$http', '$state', '$element',
    function ($scope, $http, $state) {
        $scope.load = function () {
            $scope.loading = true;
            $scope.error = null;
            $scope.result = null;
            $http.get("/activity/search", {
                params: $scope.buildParam()
            }).success(function (result) {
                $scope.loading = false;
                if (result.success === false) {
                    $scope.error = result.message;
                }
                $scope.result = result.data;
                $scope.pagination.pages = Math.ceil($scope.result.total / $scope.pagination.size);
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.error = data.message || status;
            });
        }

        $scope.reload = function () {
            $scope.pagination.page = 1;
            $scope.load();
        }

        $scope.loadPage = function (page) {
            $scope.pagination.page = page;
            $scope.load();
        }

        $scope.result = {
            rows: []
        }

        $scope.pagination = {
            size: 20,
            page: 1,
            pages: 0
        };


        $scope.format = function (value) {
            if (value.indexOf(":") < 0) return value;
            var values = vlaue.split(":");
            return "<a href='/fs' owner='_blank'></a>";
        }

        $scope.onPageChange = function () {
            if ($scope.pagination.page > $scope.pagination.pages) return;
            $scope.reload();
        }

        $scope.condition = {
            start: null,
            end: null,
            operator: null,
            fileName: null,
            operation: null
        }

        $scope.buildParam = function () {
            var start = $scope.condition.start;
            var end = $scope.condition.end;
            var param = {
                size: $scope.pagination.size,
                page: $scope.pagination.page,
                startDate: start ? moment(start).format("YYYY-MM-DD") + " 00:00:00" : null,
                endDate: end ? moment(end).format("YYYY-MM-DD") + " 23:59:59" : null,
                operatorName: $scope.condition.operator,
                documentName: $scope.condition.fileName,
                operation: $scope.condition.operation,
            };
            return param;
        }
        $scope.load();

    }]);

dashboard.controller('PermissionConfigCtrl', [
    '$scope',
    '$http',
    'Messager',
    '$modal',
    'toaster',
    function ($scope, $http, Messager, $modal, toaster) {
        function load() {
            $scope.loading = true;
            $http.get("conf/permission").success(
                function (result, status, headers, config) {
                    $scope.loading = false;
                    if (result.success) {
                        $scope.permissions = result.data;
                    } else {
                        $scope.error = result.message;
                    }
                }).error(function (result, status) {
                $scope.loading = false;
                $scope.error = result.message || status;
            });
        }

        load();

        $scope.remove = function (permission) {
            var url = "conf/permission/delete";
            Messager.confirm("提示", "删除之后已配置的文件权限将会失效，确定要删除?").then(function (result) {
                var toast = $scope.toaster.wait("正在删除...");
                $http.post(url, {
                    id: permission.id
                }).success(function (result, status, headers, config) {
                    if (result.success) {
                        toast.doSuccess("权限配置已删除");
                        load();
                    } else {
                        toast.doError("删除失败：" + result.message);
                    }
                }).error(function (result, status) {
                    toast.doError("删除失败：" + (result.message || status));
                });
            });
        }

        $scope.edit = function (permission) {
            open({
                tpl: 'asset/tpl/dashboard/security/permission_form.html',
                ctrl: 'PermissionFormCtrl',
                permission: permission
            });
        }

        $scope.add = function () {
            open({
                tpl: 'asset/tpl/dashboard/security/permission_form.html',
                ctrl: 'PermissionFormCtrl'
            });
        }

        function open(config) {
            var modalInstance = $modal.open({
                templateUrl: config.tpl,
                controller: config.ctrl,
                backdrop: 'static',
                resolve: {
                    permission: function () {
                        return config.permission;
                    }
                }
            });

            modalInstance.result.then(function () {
                load();
            }, function () {

            });
        }
    }]);

dashboard.controller('PermissionFormCtrl', [
    '$scope',
    '$http',
    'permission',
    '$modalInstance',
    'Messager',
    function ($scope, $http, permission, $modalInstance, Messager) {

        $scope.permissions = {
            upload: false,
            download: false,
            create: false,
            move: false,
            copy: false,
            lock: false,
            edit: false,
            remove: false,
            revert: false,
            view: false,
            share: false,
            review: false
        };

        $scope.checkAll = function (isCheckAll) {
            for (var authority in $scope.permissions) {
                $scope.permissions[authority] = isCheckAll;
            }
        }

        $scope.permission = permission;
        $scope.operation = '新建'
        if (permission != null) {
            if ($scope.permission.permissions) {
                var permissions = $scope.permission.permissions.split(",");
                for (var i = 0; i < permissions.length; i++) {
                    $scope.permissions[permissions[i]] = true;
                }
            }
            /*var toast = $scope.toaster.wait("正在加载权限配置...");
            $http.get("conf/permissionId/" + id).success(function(result) {
                if(result.success){
                    $scope.permissionId = result.permissionId;
                    var authorities = $scope.permissionId.authorities.split(",");
                    for(var i = 0 ;i < authorities.length;i ++){
                        $scope.authorities[authorities[i]] = true;
                    }
                    toast.doClose();
                }else{
                    toast.doError("加载权限配置失败", result.message);
                }
            }).error(function(result,status){
                toast.doError("加载权限配置失败：" + (result.message || status) );
            });*/
            $scope.operation = '编辑';
        }

        $scope.save = function () {
            var url = 'conf/permission/save';
            if (permission.id != null) {
                url = 'conf/permission/update';
            }
            var permissions = [];
            $scope.saving = true;
            for (var authority in $scope.permissions) {
                if ($scope.permissions[authority] === true) {
                    permissions.push(authority);
                }
            }
            $scope.permission.permissions = permissions.join(",");
            var toast = $scope.toaster.wait("正在保存...");
            $http.post(url, $scope.permission).success(function (result) {
                $scope.saving = false;
                if (result.success) {
                    toast.doSuccess("保存成功");
                    $modalInstance.close();
                } else {
                    toast.doError("保存失败：" + result.message);
                }
            }).error(function (result, status) {
                $scope.saving = false;
                toast.doError("保存失败：" + (result.message || status));
            });
        }

        $scope.cancel = function () {
            if ($scope.permissionConfigForm.$dirty) {
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
