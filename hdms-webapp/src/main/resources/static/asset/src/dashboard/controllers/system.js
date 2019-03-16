dashboard.controller('NoticeCtrl', [
    '$scope',
    '$http',
    'Messager',
    '$rootScope', '$filter', function ($scope, $http, Messager, $rootScope, $filter) {
        function load() {
            var toast = $scope.toaster.wait("正在加载通知");
            $http.get("/notice").success(
                function (result, status, headers, config) {
                    if (result.success) {
                        toast.doClose();
                        $scope.notice = result.data;
                    } else {
                        toast.doError("加载通知失败：" + result.message);
                    }
                }).error(function (result, status) {
                toast.doError("加载通知失败：" + (result.message || status));
            });
        }

        load();

        $scope.save = function () {
            $scope.notice.startDate = $filter("date")($scope.notice.startDate, "yyyy-MM-dd HH:mm:ss");
            $scope.notice.endDate = $filter("date")($scope.notice.endDate, "yyyy-MM-dd HH:mm:ss");
            $http.post("notice", $scope.notice).success(
                function (data, status, headers, config) {
                    if (data.success) {
                        $scope.settingForm.$setPristine();
                        $scope.settingForm.$setUntouched();
                        $rootScope.toaster.success("保存成功");
                    } else {
                        $rootScope.toaster.error("保存失败：" + data.message);
                    }
                }).error(function (data, status) {
                $rootScope.toaster.error('保存失败：' + (data.message || status));
            });
        }

        $scope.reset = function () {
            $scope.settingForm.$setPristine();
            $scope.settingForm.$setUntouched();
            load();
        }
    }]);

/* Controllers */
// signin controller
dashboard.controller('SettingCtrl', ['$scope', '$http', '$state', '$rootScope', function ($scope, $http, $state, $rootScope) {

    $scope.watermarkProperties = {
        name: false,
        dept: false,
        date: false,
        time: false,
        fileName: false
    };

    function load() {
        $http.get("/setting").success(
            function (result, status, headers, config) {
                if (result.success) {
                    processSetting(result.data);
                } else {
                    $rootScope.toaster.pop('error', '提示', "加载安全配置失败：" + data.message);
                }
            }).error(function () {
            $rootScope.toaster.pop('error', '提示', '加载安全配置失败：' + status.statusCode);
        });
    }

    load();

    function processSetting(setting) {
        if (setting.watermarkProperties) {
            Object.keys($scope.watermarkProperties).forEach(function (key) {
                if (setting.watermarkProperties.indexOf(key) >= 0) {
                    $scope.watermarkProperties[key] = true;
                }
            });
        }
        $scope.setting = setting;
    }

    $scope.reset = function () {
        $scope.securitySettingForm.$setPristine();
        $scope.securitySettingForm.$setUntouched();
        load();
    }

    $scope.save = function () {
        $scope.setting.watermarkProperties = "";
        Object.keys($scope.watermarkProperties).forEach(function (key) {
            if ($scope.watermarkProperties[key] === true) {
                $scope.setting.watermarkProperties += key + ";";
            }
        });
        $http.post("/setting", $scope.setting).success(
            function (result, status, headers, config) {
                if (result.success) {
                    $scope.securitySettingForm.$setPristine();
                    $scope.securitySettingForm.$setUntouched();
                    $rootScope.toaster.pop('success', '提示', "保存成功");
                } else {
                    $rootScope.toaster.pop('error', '提示', "保存失败：" + result.message);
                }
            }).error(function () {
            $rootScope.toaster.pop('error', '提示', '保存设置失败：' + status.statusCode);
        });
    }
}]);

