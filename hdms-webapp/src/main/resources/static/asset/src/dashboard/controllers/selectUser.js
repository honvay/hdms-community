dashboard.controller('SelectUserCtrl', [
    '$rootScope',
    '$scope',
    '$http',
    '$modal',
    '$modalInstance',
    'options',
    function ($rootScope, $scope, $http, $modal, $modalInstance, options) {
        $scope.loading = false;
        $scope.tree = {};
        $scope.onTreeInit = function () {
            var root = $scope.tree.getRoot();
            $scope.tree.select(root);
        }
        $scope.multiple = options.multiple;

        $scope.clear = function () {
            $scope.searching = false;
            $scope.condition = null;
        }

        $scope.selected = [];
        $scope.selectedIds = [];
        $scope.select = function (user) {
            if ($scope.selectedIds.indexOf(user.id) >= 0) {
                return;
            }
            if ($scope.multiple === false) {
                $scope.selected = [];
                $scope.selectedIds = [];
            }
            $scope.selected.push(user);
            $scope.selectedIds.push(user.id);
        }

        $scope.remove = function (user) {
            if ($scope.selectedIds.indexOf(user.id) < 0) {
                return;
            }
            $scope.selected.splice($scope.selected.indexOf(user), 1);
            $scope.selectedIds.splice($scope.selectedIds.indexOf(user.id), 1);
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
                    $scope.result = data.data;
                } else {
                    $scope.toaster.pop('error', '提示', data.message);
                }
            }).error(function (data, status) {
                $scope.loading = false;
                $scope.toaster.pop('error', '提示', '加载失败：' + status);
            });
        }

        $scope.ok = function () {
            $modalInstance.close($scope.selected);
        }

        $scope.loadUser = function (dept) {
            $scope.loading = true;
            $http.get("/user/list", {
                params: {
                    departmentId: dept.id
                }
            }).success(function (result, status, headers, config) {
                $scope.loading = false;
                console.log(result);
                if (result.success) {
                    $scope.users = result.data;
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
