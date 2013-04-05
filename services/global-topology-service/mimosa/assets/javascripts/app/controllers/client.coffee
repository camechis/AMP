
window.ClientListCtrl = ($scope, $http, $route, $location) ->
	
	$scope.clients = $http.get("/service/route/clients")
		.success (data) ->
			$scope.clients = data


window.ClientViewCtrl = ($scope, $http, $location, $routeParams) ->
	
	$scope.clientId = $routeParams.clientId
	$scope.routes = $http.get("/service/route/client/#{$routeParams.clientId}")
		.success (data) ->
			$scope.routes = data