app.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'asset/l10n/',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('zh_CN');
}]).factory('responseObserver', ['$q', '$window', function responseObserver($q, $window) {
    return {
        'responseError': function (errorResponse) {
            switch (errorResponse.status) {
                case 401:
                    $window.location.href = '/login';
                    break;
            }
            return $q.reject(errorResponse);
        }
    };
}]).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('responseObserver');
    var defaultHeaders = {
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest',
    };
    defaultHeaders[crsf.headerName] = crsf.token;
    $httpProvider.defaults.headers.post = defaultHeaders;
    $httpProvider.defaults.headers.get = {
        'X-Requested-With': 'XMLHttpRequest'
    };
}]);
app.run(['$rootScope', '$state', '$stateParams', '$http', 'Messager',
    function ($rootScope, $state, $stateParams, $http, Messager) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    }])
    .config(['$stateProvider', '$urlRouterProvider', '$locationProvider',
        function ($stateProvider, $urlRouterProvider, $locationProvider) {
            $locationProvider.html5Mode(true);
            $urlRouterProvider.otherwise('/index/directory/enterprise');
            //路由配置
            $urlRouterProvider.when('/index/document/{document}', ['$state', '$match', '$rootScope', function ($state, $match, $rootScope) {
                $rootScope.fid = $match.document;
            }]);
            $stateProvider.state('favorites', {
                url: '/index/favorites',
                templateUrl: 'asset/tpl/app/favorites.html',
                controller: 'FavoritesCtrl'
            }).state('recycle', {
                url: '/index/recycle',
                templateUrl: 'asset/tpl/app/recycle.html',
                controller: 'RecycleCtrl'
            }).state('recent', {
                url: '/index/recent',
                templateUrl: 'asset/tpl/app/recent.html',
                controller: 'RecentsCtrl'
            }).state('share', {
                url: '/index/share',
                templateUrl: 'asset/tpl/app/shares.html',
                controller: 'ShareCtrl'
            }).state('search', {
                url: '/index/search/{keyword}',
                templateUrl: 'asset/tpl/app/search.html',
                controller: 'SearchCtrl'
            }).state('directory', {
                url: '/index/directory/{directory}',
                templateUrl: 'asset/tpl/app/directory.html',
                controller: 'DirectoryCtrl'
            }).state('file', {
                url: '/index/directory/{directory}/{file}'
            }).state('changePassword', {
                url: '/index/changePassword',
                templateUrl: 'asset/tpl/app/change_password.html',
                controller: 'ChangePasswordCtrl'
            }).state('profile', {
                url: '/index/profile',
                templateUrl: 'asset/tpl/app/profile.html',
                controller: 'ProfileCtrl'
            }).state('messages', {
                url: '/index/messages',
                templateUrl: 'asset/tpl/app/messages.html',
                controller: 'MessagesCtrl'
            });
        }
    ]);
