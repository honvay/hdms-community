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
