'use strict';

/*
 * AngularJS Toaster
 * Version: 0.4.8
 *
 * Copyright 2013 Jiri Kavulak.  
 * All Rights Reserved.  
 * Use, reproduction, distribution, and modification of this code is subject to the terms and 
 * conditions of the MIT license, available at http://www.opensource.org/licenses/mit-license.php
 *
 * Author: Jiri Kavulak
 * Related to project of John Papa and Hans Fjällemark
 */
angular.module('toaster', ['ngAnimate'])
.service('toaster', ['$rootScope', function ($rootScope) {
    this.pop = function (type, title, body, timeout, bodyOutputType, clickHandler,closeButton) {
    	if(angular.isObject(arguments[0])){
    		this.toast = arguments[0];
    	}else{
	        this.toast = {
	            type: type,
	            title: title,
	            body: body,
	            timeout: timeout,
	            bodyOutputType: bodyOutputType,
	            clickHandler: clickHandler,
	            closeButton : closeButton
	        };
    	}
        $rootScope.$broadcast('toaster-newToast');
        return this.toast;
    };
    
    this.wait = function(options){
    	if(angular.isString(options)){
    		options = {
    			title : options
    		}
    	}
    	angular.extend(options,{
    		type : 'wait',
    		timeout:-1,
    		closeButton : false
    	});
    	return this.pop(options);
    }
    
    this.error = function(options){
    	if(angular.isString(options)){
    		options = {
    			title : options
    		}
    	}
    	angular.extend(options,{
    		type : 'error',
    		timeout:-1,
    		closeButton : true
    	});
    	return this.pop(options);
    }
    
    this.success = function(options){
    	if(angular.isString(options)){
    		options = {
    			title : options
    		}
    	}
    	angular.extend(options,{
    		type : 'success',
    		closeButton : true
    	});
    	return this.pop(options);
    }
    
    this.close = function(toast){
    	$rootScope.$broadcast('toaster-closeToast',toast);
    }
    
    this.update = function(toast,options){
    	$rootScope.$broadcast('toaster-updateToast',toast,options);
    }

    this.clear = function () {
        $rootScope.$broadcast('toaster-clearToasts');
    };
}])
.constant('toasterConfig', {
    'limit': 0,                   // limits max number of toasts 
    'tap-to-dismiss': true,
    'close-button': false,
    'newest-on-top': true,
    'fade-in': 10,            // done in css
    //'on-fade-in': undefined,    // not implemented
    //'fade-out': 1000,           // done in css
    // 'on-fade-out': undefined,  // not implemented
    //'extended-time-out': 1000,    // not implemented
    'time-out': 3000, // Set timeOut and extendedTimeout to 0 to make it sticky
    'icon-classes': {
        error: 'toast-error',
        info: 'toast-info',
        wait: 'toast-wait',
        success: 'toast-success',
        warning: 'toast-warning'
    },
    'body-output-type': '', // Options: '', 'trustedHtml', 'template'
    'body-template': 'toasterBodyTmpl.html',
    'icon-class': 'toast-info',
    'position-class': 'toast-top-right',
    'title-class': 'toast-title',
    'message-class': 'toast-message'
})
.directive('toasterContainer', ['$compile', '$timeout', '$sce', 'toasterConfig', 'toaster',
function ($compile, $timeout, $sce, toasterConfig, toaster) {
    return {
        replace: true,
        restrict: 'EA',
        scope: true, // creates an internal scope for this directive
        link: function (scope, elm, attrs) {

            var id = 0,
                mergedConfig;

            mergedConfig = angular.extend({}, toasterConfig, scope.$eval(attrs.toasterOptions));

            scope.config = {
                position: mergedConfig['position-class'],
                title: mergedConfig['title-class'],
                message: mergedConfig['message-class'],
                tap: mergedConfig['tap-to-dismiss'],
                closeButton: mergedConfig['close-button']
            };

            scope.configureTimer = function configureTimer(toast) {
                var timeout = typeof (toast.timeout) == "number" ? toast.timeout : mergedConfig['time-out'];
                if (timeout > 0)
                    setTimeout(toast, timeout);
            };
            
            function updateToast(options){
            	var timeout = this.timeout;
            	options.type = mergedConfig['icon-classes'][options.type];
            	angular.extend(this,options);
            	if(this.timeout < 0){
            		scope.stopTimer(this);
            	}else if(!this.timeout){
            		scope.configureTimer(this)
            	}else if(this.timeout){
            		scope.restartTimer(this)
            	}
            }
            
            function doWait(options){
            	if(angular.isString(options)){
            		options = {
            			title : options
            		}
            	}
            	angular.extend(options,{
            		type : 'wait',
            		timeout:-1,
            		closeButton : false
            	});
            	this.update(options);
            }
            
           function doError(options){
        	   if(angular.isString(options)){
	           		options = {
	           			title : options
	           		}
	           	}
            	angular.extend(options,{
            		type : 'error',
            		timeout:-1,
            		closeButton : true
            	});
            	this.update(options);
            }
            
            function doSuccess(options){
            	if(angular.isString(options)){
            		options = {
            			title : options
            		}
            	}
            	angular.extend(options,{
            		type : 'success',
            		timeout:3000,
            		closeButton : true
            	});
            	this.update(options);
            }
            
            function doClose(){
            	scope.removeToast(this.id);
            }

            function addToast(toast) {
                toast.type = mergedConfig['icon-classes'][toast.type];
                if (!toast.type)
                    toast.type = mergedConfig['icon-class'];

                id++;
                angular.extend(toast, { id: id });
                toast.update = updateToast;
                toast.doWait = doWait;
                toast.doSuccess = doSuccess;
                toast.doError = doError;
                toast.doClose = doClose;
                // Set the toast.bodyOutputType to the default if it isn't set
                toast.bodyOutputType = toast.bodyOutputType || mergedConfig['body-output-type'];
                toast.closeButton = angular.isDefined(toast.closeButton) ? toast.closeButton : scope.config.closeButton;
                switch (toast.bodyOutputType) {
                    case 'trustedHtml':
                        toast.html = $sce.trustAsHtml(toast.body);
                        break;
                    case 'template':
                        toast.bodyTemplate = toast.body || mergedConfig['body-template'];
                        break;
                }

                scope.configureTimer(toast);

                if (mergedConfig['newest-on-top'] === true) {
                    scope.toasters.unshift(toast);
                    if (mergedConfig['limit'] > 0 && scope.toasters.length > mergedConfig['limit']) {
                        scope.toasters.pop();
                    }
                } else {
                    scope.toasters.push(toast);
                    if (mergedConfig['limit'] > 0 && scope.toasters.length > mergedConfig['limit']) {
                        scope.toasters.shift();
                    }
                }
            }

            function setTimeout(toast, time) {
                toast.timeout = $timeout(function () {
                    scope.removeToast(toast.id);
                }, time);
            }

            scope.toasters = [];
            scope.$on('toaster-updateToast', function (event,toast,options) {
            	updateToast(toast,options);
            });
            scope.$on('toaster-closeToast', function (toast) {
            	scope.removeToast(toast.id);
            });
            scope.$on('toaster-newToast', function () {
                addToast(toaster.toast);
            });

            scope.$on('toaster-clearToasts', function () {
                scope.toasters.splice(0, scope.toasters.length);
            });
        },
        controller: ['$scope', '$element', '$attrs', function ($scope, $element, $attrs) {

            $scope.stopTimer = function (toast) {
                if (toast.timeout) {
                    $timeout.cancel(toast.timeout);
                    toast.timeout = null;
                }
            };

            $scope.restartTimer = function (toast) {
                if (toast.timeout){
                    $scope.configureTimer(toast);
                }
            };

            $scope.removeToast = function (id) {
                var i = 0;
                for (i; i < $scope.toasters.length; i++) {
                    if ($scope.toasters[i].id === id)
                        break;
                }
                $scope.toasters.splice(i, 1);
            };

            $scope.click = function (toaster) {
                if ($scope.config.tap === true) {
                    if (toaster.clickHandler && angular.isFunction($scope.$parent.$eval(toaster.clickHandler))) {
                        var result = $scope.$parent.$eval(toaster.clickHandler)(toaster);
                        if (result === true)
                            $scope.removeToast(toaster.id);
                    } else {
                        if (angular.isString(toaster.clickHandler))
                            console.log("TOAST-NOTE: Your click handler is not inside a parent scope of toaster-container.");
                        $scope.removeToast(toaster.id);
                    }
                }
            };
        }],
        template:
        '<div  id="toast-container" ng-class="config.position">' +
            '<div ng-repeat="toaster in toasters" class="toast" ng-class="toaster.type" ng-click="click(toaster)" ng-mouseover="stopTimer(toaster)"  ng-mouseout="restartTimer(toaster)">' +
              '<button class="toast-close-button" ng-show="toaster.closeButton">&times;</button>' +
              '<div ng-class="config.title">{{toaster.title}}</div>' +
              '<div ng-class="config.message" ng-switch on="toaster.bodyOutputType">' +
                '<div ng-switch-when="trustedHtml" ng-bind-html="toaster.html"></div>' +
                '<div ng-switch-when="template"><div ng-include="toaster.bodyTemplate"></div></div>' +
                '<div ng-switch-default >{{toaster.body}}</div>' +
              '</div>' +
            '</div>' +
        '</div>'
    };
}]);
