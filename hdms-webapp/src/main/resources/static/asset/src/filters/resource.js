'use strict';
/* Filters */
// need load the moment.js to use this filter.
angular.module('hc.util').filter('resource', ['$rootScope',function($rootScope) {
	return function(resource, type) {
		return resource;
	}
}]);