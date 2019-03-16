app.factory('SocketIO',['$rootScope','FileSystem','$modal',function($rootScope,FileSystem,$modal) {
	
	var service = {};
	
	service.init = function($scope){
		if(window.WebSocket){
			var websocket = new WebSocket("ws://localhost:8080/socket.io");
			websocket.onopen = function(){
				
			}
			websocket.onclose = function(){
				
			}
			websocket.onerror = function(){
				
			}
		}
	}
	
	return service;
}]);