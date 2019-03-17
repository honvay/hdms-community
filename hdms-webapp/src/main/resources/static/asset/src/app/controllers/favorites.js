app.controller( 'FavoritesCtrl',
				['$rootScope','$scope','$http','$state','$location','$modal','Messager','FileSystem',
	function($rootScope, $scope, $http, $state, $location,
			$modal, Messager, FileSystem) {
	
		$rootScope.module = 'favorites';
					
		/** *************** 加载文件 ****************** */
		$scope.load = function() {
			$scope.loading = true;
			$http.get('/fs/favorites').success(function(result){
				$scope.loading = false;
				if(result.success){
					$scope.files = result.data;
				}else{
					$scope.error = result.message;
				}
			}).error(function(cause,status){
				$scope.error = (cause.message || status);
				$scope.toaster.pop("error",(cause.message || status));
				$scope.loading = false;
			});
		};
		
		$scope.$on("$viewContentLoaded", function() {
			$scope.load();
		});
	
		$scope.removeFavorite = function(){
			Messager.confirm("提示","确定要删除?").then(function(){
				var toast = $rootScope.toaster.wait("正在删除...");
				FileSystem.removeFavorite($scope.selection).then(function(result){
					toast.doSuccess("已删除");
					$scope.load();
				},function(cause){
					toast.doError("删除失败:" + cause);
				});
			});
		};
		
	
		/** ********* 文件选择 *************** */
		$scope.isSelectAll = false;
		$scope.selection = [];
	
		$scope.selectAll = function() {
			$scope.isSelectAll = !$scope.isSelectAll;
			var len = $scope.files.length;
			for (var i = 0; i < len; i++) {
				$scope.select($scope.files[i],
						$scope.isSelectAll);
			}
		};
	
		$scope.addSelection = function(file) {
			$scope.selection.push(file);
			file.selected = true;
			if ($scope.selection.length === $scope.files.length) {
				$scope.isSelectAll = true;
			}
		};
	
		$scope.removeSelection = function(file) {
			$scope.selection.splice($scope.selection.indexOf(file), 1);
			file.selected = false;
			$scope.isSelectAll = false;
		};
	
		$scope.clearSelection = function() {
			$scope.selection.forEach(function(file) {
				file.selected = false;
			});
			$scope.selection = [];
			$scope.isSelectAll = false;
		};
	
		$scope.select = function(file, selected, event) {
			if (event && $scope.mode == 'block' && !event.ctrlKey) {
				$scope.clearSelection();
			}
	
			if (event) {
				event.stopPropagation();
			}
	
			if (file.selected && selected === file.selected) {
				return;
			}
			selected = selected || (file.selected ? !file.selected : true);
			return selected === false ? $scope.removeSelection(file) : $scope.addSelection(file);
		};
	
		$scope.onContainerClick = function() {
			if ($scope.mode == 'block') {
				$scope.clearSelection();
			}
		};
	
		// 删除文件
		$scope.remove = function(file) {
			if (!file) {
				file = $scope.selection;
				if (file.length === 0) {
					Messager.alert("提示", "请选择要删除的文件");
				}
			}
			
			$rootScope.remove(file,function(){
				$scope.reload();
			});
		};
	
		$scope.reset = function() {
			$scope.selection = [];
			$scope.isSelectAll = false;
			// $rootScope.current = null;
		};
		
		function handleF5(event){
		    var keycode = event.keyCode;  
		    if(keycode === 116){
		    	event.preventDefault();  
		    	$scope.load();
		    }  
		}
		
		$(document).on("keydown", handleF5);
	
		$scope.$on("$destroy", function() {
			$(document).unbind("keydown", handleF5);
		});
} ]);
