'use strict';
var dashboard = angular.module('dashboard', [
    'ngAnimate',
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'ui.validate',
    'pascalprecht.translate',
    'messager',
    'toaster',
    'hc.ui',
    'hc.util',
    'ui.jq'
]);

dashboard.config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
        function ($controllerProvider, $compileProvider, $filterProvider, $provide) {
            // lazy controller, directive and service
            dashboard.controller = $controllerProvider.register;
            dashboard.filter = $filterProvider.register;
            dashboard.factory = $provide.factory;
            dashboard.service = $provide.service;
            dashboard.constant = $provide.constant;
            dashboard.value = $provide.value;
        }
    ]);

if (window.require) {
    (function (factory) {

        if (typeof define === "function" && define.amd) {
            // AMD. Register as an anonymous module.
            require.config({
                paths: {
                    'dashboard': '/asset/src/dashboard'
                },
                urlArgs: '_dc=' + (new Date()).getTime()
            });
            define(['dashboard/dashboard.config',"dashboard/controllers/dashboard","dashboard/controllers/index",
                "dashboard/controllers/security","dashboard/controllers/system",
                "dashboard/controllers/department","dashboard/controllers/user"], factory);
        } else {
            // Browser globals
            factory();
        }
    }(function () {

    }));
}