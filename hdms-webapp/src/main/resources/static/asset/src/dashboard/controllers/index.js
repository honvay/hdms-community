dashboard.controller('IndexCtrl', [
    '$scope',
    '$http',
    '$state',
    'toaster', function ($scope, $http, $state, toaster) {
        $scope.report = {
            sumOfSize: '-',
            countOfFile: '-'
        }
        $http.get("/report").success(function (result) {
            if (result.success) {
                $scope.report = result.data;
            } else {
                toaster.error("获取报表数据失败");
            }
        }).error(function () {
            toaster.error("获取报表数据失败");
        });
        $scope.getPercent = function (space) {
            return Math.ceil((space.used / space.total) * 100);
        }
        $scope.getType = function(space){
            var value = (space.used / space.total) * 100;
            var type;
            if (value < 25) {
                type = 'success';
            } else if (value < 50) {
                type = 'info';
            } else if (value < 75) {
                type = 'warning';
            } else {
                type = 'danger';
            }
            return type;
        }
        $scope.getColor = function(space){
            var value = (space.used / space.total) * 100;
            var color;
            if (value < 25) {
                color = '#27c24c';
            } else if (value < 50) {
                color = '#23b7e5';
            } else if (value < 75) {
                color = '#fad733';
            } else {
                color = '#f05050';
            }
            return color;
        }

    }]);