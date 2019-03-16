dashboard.config(['$translateProvider', function ($translateProvider) {
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
        prefix: '/asset/l10n/',
        suffix: '.json'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('zh_CN');
    // Tell the module to store the language in the local storage
    //$translateProvider.useLocalStorage();
}]).config(['$httpProvider', function ($httpProvider) {
    var headers = {
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
    };
    headers[crsf.headerName] = crsf.token;
    $httpProvider.defaults.headers.post = headers;
    $httpProvider.defaults.headers.get = {
        'X-Requested-With': 'XMLHttpRequest'
    }
}]);
dashboard.run(
    ['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ]
).config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/index');
            $stateProvider
                .state('/index', {
                    url: '/index',
                    templateUrl: 'asset/tpl/dashboard/index.html',
                    controller: 'IndexCtrl'
                })
                .state("organization", {
                    url: '/organization',
                    templateUrl: 'asset/tpl/dashboard/organization/organization.html',
                })
                .state('security', {
                    url: '/security',
                    template: '<div ui-view class="app-view fade-in-up"></div>'
                })
                .state('security.loginLog', {
                    url: '/login/log',
                    templateUrl: 'asset/tpl/dashboard/security/login_log.html'
                }).state('security.activity', {
                url: '/activity',
                templateUrl: 'asset/tpl/dashboard/security/activity.html'
            }).state('sys', {
                abstract: true,
                url: '/sys',
                template: '<div ui-view class="app-view"></div>'
            }).state('sys.setting', {
                url: '/setting',
                templateUrl: 'asset/tpl/dashboard/system/setting.html'
            }).state('sys.task', {
                url: '/task',
                templateUrl: 'asset/tpl/dashboard/system/task.html'
            }).state('sys.notice', {
                url: '/notice',
                templateUrl: 'asset/tpl/dashboard/system/notice.html'
            })
        }
    ]
);

