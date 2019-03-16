app.controller('DropboxCtrl', [
            		'$scope',
            		'$http',
            		'data',
            		'$modalInstance',
            		'Messager',
            		function($scope, $http, data, $modalInstance, Messager) {
            			$scope.files = [];
            			for(var i = 0; i < data.files.length;i++){
            			    console.log(data.files);
            				$scope.files.push(data.files[i]);
            			}
            			$scope.treeConfig = {
            				'load-url' : '/fs/folder',
            				'load-asyn' : true,
            				'open-folder-icon':'fa fa-users',
            			    'folder-icon':'fa fa-users',
            			    'leaf-icon':'fa fa-folder',
            			}
            			
            			$scope.setParam = function(node, params) {
            				params.mount = node.mount;
            				params.permission = 'upload';
            			};
            			$scope.directory = data.directory;
            			//默认上传到个人文档
            			if(!$scope.directory){
            				$scope.directory = {
            					mountAlias : 'my',
            					mount : $scope.mounts.my,
            					name : '我的文档',
            					fullName : '我的文档'
            				}
            			}
            			//TODO 判断企业文件夹不能上传文件
            			$scope.onChange = function(node){
            				if(!node.id && node.mount === $scope.mounts.enterprise){
            					$scope.allowed = false;
            				}else{
            					$scope.allowed = true;
            				}
            			}
            			
            			$scope.remove = function(file){
            				$scope.files.splice($scope.files.indexOf(file),1);
            				if($scope.files.length === 0){
            					$modalInstance.dismiss('cancel');
            				}
            			}
            			
            			$scope.ok = function(){
            				var uploadContext = {
                                mountAlias : $scope.directory.id || ($scope.mountAliases[$scope.directory.mount]),
            					parent : $scope.directory.id,
            					path : $scope.directory.name,
            					mount : $scope.directory.mount
            				};
            				$modalInstance.close({
            					files : $scope.files,
            					uploadContext : uploadContext
            				});
            			}

            			$scope.cancel = function() {
        					Messager.confirm("提示", "是否确定放弃上传?").then(
        							function(result) {
        								$modalInstance.dismiss('cancel');
        							}, function() {
        						});
            			}
            } ]);