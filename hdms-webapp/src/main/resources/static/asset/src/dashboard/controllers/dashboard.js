dashboard.controller('DashboardCtrl', ['$rootScope', '$scope', '$translate', '$window', 'toaster',
    function ($rootScope, $scope, $translate, $window, toaster) {
        $scope.user = user;
        $rootScope.toaster = toaster;
        $rootScope.resourceServerUrl = "/";
        $rootScope.staticResourceServerUrl = "/";
    }]);
angular.element(document).ready(function () {
    angular.bootstrap(document, ['dashboard']);
});
