window.NavbarCtrl = ($scope, $route, $location) ->
	
	$scope.activeRoute = $location.path()
	
	$scope.$on('$routeChangeStart', (scope, next, current) ->
		$scope.activeRoute = $location.path())