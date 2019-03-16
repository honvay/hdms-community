app.controller('MessagesCtrl', ['$scope', '$http', '$rootScope', '$location', '$state',
    function ($scope, $http, $rootScope, $location, $state) {
        if (!$scope.message) {
            $scope.$on("$viewContentLoaded", function () {
                $scope.load();
            });
        }
        $scope.load = function () {
            $scope.loading = true;
            $http.get("/message/list").success(function (result) {
                $scope.loading = false;
                if (result.success) {
                    $rootScope.messages = result.data;
                } else {
                    $scope.error = "加载消息失败:" + result.message;
                }
            }).error(function (result, status) {
                $scope.loading = false;
                $scope.error = "加载消息失败:" + (result.message || status);
            });
        }
    }]);