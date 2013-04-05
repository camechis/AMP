window.BreadcrumbsCtrl = ($scope, $route, $location, $http) ->
	
	$scope.user = $http.get("/service/users/who-am-i").success((data) -> $scope.user = data.user)
	
	parseRoute = () ->
		$scope.blocks = []
		routeBlocks = $location.path().split("/")
		concatenatedRoute = ""
		for routeBlock in routeBlocks
			unless routeBlock is ""
				name = routeBlock
				concatenatedRoute += "/" + routeBlock
				$scope.blocks.push {name:name, path:concatenatedRoute}
				
	parseRoute()
	
	$scope.$on('$routeChangeStart', (scope, next, current) -> parseRoute())