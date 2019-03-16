app.controller('ChangePasswordCtrl', ['$scope','$http', function ($scope,$http) {
      $scope.check = function(){
		    var modes = 0;
		    //正则表达式验证符合要求的
		    if ($scope.newPassword.length < 1) return modes;
		    if (/\d/.test($scope.newPassword)) modes++; //数字
		    if (/[a-z]/.test($scope.newPassword)) modes++; //小写
		    if (/[A-Z]/.test($scope.newPassword)) modes++; //大写
		    if (/\W/.test($scope.newPassword)) modes++; //特殊字符
		   $scope.strength = modes;
      };
      
      $scope.submit = function(){
    	  $http.post('/user/change/password',{
              oldPassword:$scope.oldPassword,
              newPassword : $scope.newPassword,
              confirmPassword : $scope.confirmPassword
          }).success(function(result){
    		  if(result.success){
    			  $scope.newPassword = null;
    			  $scope.oldPassword = null;
    			  $scope.confirmPassword = null;
    			  $scope.success = true;
    			  $scope.cause = null;
    			  $scope.user.changePassword = false;
    		  }else{
    			  $scope.cause = result.message;
    			  $scope.success = false;
    		  }
    	  }).error(function(result,status){
    		  $scope.error  = result.message || status;
    		  $scope.success = false;
    	  });
      };
  }]);