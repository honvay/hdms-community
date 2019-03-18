var app = angular.module('app', [
    'ngAnimate',
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'ui.validate',
    'pascalprecht.translate',
    'messager',
    'toaster',
    'hc.util',
    'hc.ui',
    'hc.common',
    'ui.codemirror'
]);

app.config(
    ['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
        function ($controllerProvider, $compileProvider, $filterProvider, $provide) {
            // lazy controller, directive and service
            app.controller = $controllerProvider.register;
            app.directive = $compileProvider.directive;
            app.filter = $filterProvider.register;
            app.factory = $provide.factory;
            app.service = $provide.service;
            app.constant = $provide.constant;
            app.value = $provide.value;
        }
    ]);

if (window.require) {
    (function (factory) {

        if (typeof define === "function" && define.amd) {
            // AMD. Register as an anonymous module.
            require.config({
                paths: {
                    'app': '/asset/src/app'
                },
                urlArgs: '_dc=' + (new Date()).getTime()
            });
            define(['app/app.config', "app/services/fileSystem",
                "app/services/webUploader","app/services/dropbox",
                "app/services/fileOperation","app/controllers/app",
                "app/controllers/directory", "app/controllers/document",
                "app/controllers/authorize", "app/controllers/favorites",
                "app/controllers/recents", "app/controllers/recycle",
                "app/controllers/share", "app/controllers/changePassword",
                "app/controllers/selectUser",
                "app/controllers/profile", "app/controllers/dropbox",
                "app/controllers/messages", "app/controllers/search"], factory);
        } else {
            // Browser globals
            factory();
        }
    }(function () {

    }));
}