app.controller('ShareCtrl',
				['$rootScope','$scope','$http','$state','$location','$modal','Messager','FileSystem',
	function($rootScope, $scope, $http, $state, $location,
			$modal, Messager, FileSystem) {
	
		$rootScope.module = 'share';
					
		/** *************** 加载文件 ****************** */
		$scope.load = function() {
			/*$scope.loading = true;
			$http.get('/share').success(function(result){
				$scope.loading = false;
				if(result.success){
					$scope.shares = result.shares;
				}else{
					$scope.error = result.message;
				}
			}).error(function(cause){
				$scope.error = cause;
				$scope.toaster.pop("error", "加载分享列表失败:" + cause);
				$scope.loading = false;
			});*/
		}

		$scope.$on("$viewContentLoaded", function() {
			//$scope.load();
            $scope.error = "非常抱歉，社区版暂不支持该功能";
		});
} ]);

