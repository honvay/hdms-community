app.controller( 'RecentsCtrl',
				['$rootScope','$scope','$http','$state','$location','$modal','Messager','FileSystem',
	function($rootScope, $scope, $http, $state, $location,
			$modal, Messager, FileSystem) {
	
		$rootScope.module = 'recent';
					
		/** *************** 加载文件 ****************** */
		$scope.load = function() {
			$scope.loading = true;
			$http.get('/fs/recent').success(function(result){
				$scope.loading = false;
				if(result.success){
					$scope.files = result.data;
				}else{
					$scope.error = result.message;
				}
			}).error(function(cause,status){
				$scope.error = (cause.message || status);
				$scope.toaster.pop("error", (cause.message || status));
				$scope.loading = false;
			});
		}
		
		$scope.$on("$viewContentLoaded", function() {
			$scope.load();
		});

		$scope.removeRecent = function(){
			Messager.confirm("提示","确定要删除?").then(function(){
				var toast = $rootScope.toaster.wait("正在删除...");
				var id = Util.Array.copyProperty($scope.selection,'id').join(',');
				$http.post('/recent/remove',{
					hcoId : id
				}).success(function(result){
					if(result.success){
						toast.doSuccess("已删除");
						$scope.load();
					}else{
						toast.doError("删除失败:" + result.message);
					}
				}).error(function(data,status){
					toast.doError("删除失败:" + data.message || status);
				});
			});
		}
		
		$scope.clear = function(){
			Messager.confirm("提示","确定要清除所有记录?").then(function(){
				var toast = $rootScope.toaster.wait("正在清除...");
				$http.post('/recent/clear').success(function(result){
					if(result.success){
						toast.doSuccess("已清除");
						$scope.load();
					}else{
						toast.doError("清除失败:" + result.message);
					}
				}).error(function(data,status){
					toast.doError("清除失败:" + data.message || status);
				});
			});
		}
		
	
		/** ********* 文件选择 *************** */
		$scope.isSelectAll = false;
		$scope.selection = [];
	
		$scope.selectAll = function() {
			$scope.isSelectAll = !$scope.isSelectAll;
			var len = $scope.files.length;
			for (var i = 0; i < len; i++) {
				$scope.select($scope.files[i],$scope.isSelectAll);
			}
		}
	
		$scope.addSelection = function(file) {
			$scope.selection.push(file);
			file.selected = true;
			if ($scope.selection.length === $scope.files.length) {
				$scope.isSelectAll = true;
			}
		}
	
		$scope.removeSelection = function(file) {
			$scope.selection.splice($scope.selection.indexOf(file), 1);
			file.selected = false;
			$scope.isSelectAll = false;
		}
	
		$scope.clearSelection = function() {
			$scope.selection.forEach(function(file) {
				file.selected = false;
			});
			$scope.selection = [];
			$scope.isSelectAll = false;
		}
	
		$scope.select = function(file, selected, event) {
			if (event && $scope.mode == 'block' && !event.ctrlKey) {
				$scope.clearSelection();
			}
	
			if (event) {
				$(".app-ctrl").scrollTop(0);
				event.stopPropagation();
			}
	
			if (file.selected && selected === file.selected) {
				return;
			}
			selected = selected || (file.selected ? !file.selected : true);
			selected === false ? $scope.removeSelection(file) : $scope.addSelection(file);
		}
	
		$scope.onContainerClick = function() {
			if ($scope.mode === 'block') {
				$scope.clearSelection();
			}
		}
	
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
		}
	
		$scope.reset = function() {
			$scope.selection = [];
			$scope.isSelectAll = false;
			// $rootScope.current = null;
		}
		
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
