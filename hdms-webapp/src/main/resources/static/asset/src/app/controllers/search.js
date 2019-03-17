app.controller( 'SearchCtrl',
				['$rootScope','$scope','$http','$state','$stateParams','$location','$modal','Messager','FileSystem',
	function($rootScope, $scope, $http, $state, $stateParams,$location,
			$modal, Messager, FileSystem) {
	
		$rootScope.module = 'search';
		
		$scope.keyword = $stateParams.keyword;
		$rootScope.keyword = $scope.keyword;
					
		/** *************** 加载文件 ****************** */
		$scope.load = function() {
			$scope.loading = true;
			$scope.result = [];
			$http.get('/fs/search',{
				params : $scope.buildParam()
			}).success(function(result){
				$scope.loading = false;
				if(result.success){
					//$scope.files = result.files;
					$scope.result = result.data;
					$scope.pagination.pages = result.data.pages;
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
		
		$scope.reload = function(){
			$scope.pagination.page = 1;
			$scope.load();
		}
		
		$scope.loadPage = function(page){
			$scope.pagination.page = page;
			$scope.load();
		}
		
		$scope.result = {
			rows : []
		}
		
		$scope.pagination = {
			size : 20,
			page : 1,
			pages : 0
		};
		
		$scope.onPageChange = function(){
			if($scope.pagination.page > $scope.pagination.pages) return ;
			$scope.reload();
		}
		
		$scope.buildParam = function(){
			var param = {
				size : $scope.pagination.size,
				page : $scope.pagination.page,
				keyword : $scope.keyword
			};
			return param;
		}

	
		/** ********* 文件选择 *************** */
		$scope.isSelectAll = false;
		$scope.selection = [];
	
		$scope.selectAll = function() {
			$scope.isSelectAll = !$scope.isSelectAll;
			var files = $scope.result.records;
			var len = files.length;
			for (var i = 0; i < len; i++) {
				$scope.select(files[i],$scope.isSelectAll);
			}
		}
	
		$scope.addSelection = function(file) {
			$scope.selection.push(file);
			file.selected = true;
			if ($scope.selection.length === $scope.result.records.length) {
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
			if (event && $scope.mode === 'block' && !event.ctrlKey) {
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
		
		$scope.download = function(file) {
			if(!file){
				file = $scope.selection;
			}
			$rootScope.download(file);
		};
	
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
		$(document).bind("keydown.hc",handleF5);
	
		$scope.$on("$destroy", function() {
			$(document).unbind("keydown.hc");
		});
} ]);
