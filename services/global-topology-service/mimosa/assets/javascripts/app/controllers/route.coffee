
window.RouteListCtrl = ($scope, $http, $route, $location) ->
	
	$scope.routes = $http.get("/service/route")
		.success (data) ->
			$scope.routes = data
	
	$scope.delete = (routeId) ->
		$location.path "/routes/#{routeId}/delete"
	
	$scope.edit = (routeId) ->
		$location.path "/routes/#{routeId}/edit"
	
	$scope.create = ->
		$location.path "/route/new"
		
	$scope.refresh = ->
		$route.reload()


window.RouteViewCtrl = ($scope, $http, $location, $routeParams) ->
	
	$scope.route = $http.get("/service/route/#{$routeParams.routeId}")
		.success((data) ->
			$scope.route = data)
	
	$scope.goto = (route) ->
		$location.path route

window.RouteEditCtrl = ($scope, $http, $routeParams, $location) ->
	
	$scope.route = $http.get("/service/route/#{$routeParams.routeId}")
		.success((data) ->
			$scope.route = data)
	
	$scope.action = "edit"
	
	$scope.addClient = (clientToAdd) ->
		unless _.contains($scope.route.clients, clientToAdd) or _.isUndefined clientToAdd
			$scope.route.clients.push clientToAdd
	
	$scope.editClient = (clientToEdit) ->
		$scope.route.clients = _.without $scope.route.clients, clientToEdit	
		$scope.clientToAdd = clientToEdit
	
	$scope.deleteClient = (clientToDelete) ->
		$scope.route.clients = _.without $scope.route.clients, clientToDelete	
	
	$scope.addTopic = (topicToAdd) ->
		unless _.contains($scope.route.topics, topicToAdd) or _.isUndefined topicToAdd
			$scope.route.topics.push topicToAdd

	$scope.editTopic = (topicToEdit) ->
		$scope.route.topics = _.without $scope.route.topics, topicToEdit	
		$scope.topicToAdd = topicToEdit

	$scope.deleteTopic = (topicToDelete) ->
		$scope.route.topics = _.without $scope.route.topics, topicToDelete
		
	$scope.update = ->
		$http.post("/service/route/#{$scope.route.id}", $scope.route)
			.success(-> $location.path "/routes")
			.error((data, status) -> alert("Could not update route!\nError: #{status}"))

	$scope.goBack = -> window.history.back()


window.RouteNewCtrl = ($scope, $http, $routeParams, $location) ->

	$scope.routeId = $routeParams.routeId
	
	$scope.action = "new"
	
	$scope.addClient = (clientToAdd) ->
		unless _.contains($scope.route.clients, clientToAdd) or _.isUndefined clientToAdd
			$scope.route.clients.push clientToAdd
	
	$scope.editClient = (clientToEdit) ->
		$scope.route.clients = _.without $scope.route.clients, clientToEdit	
		$scope.clientToAdd = clientToEdit
	
	$scope.deleteClient = (clientToDelete) ->
		$scope.route.clients = _.without $scope.route.clients, clientToDelete	
	
	$scope.addTopic = (topicToAdd) ->
		unless _.contains($scope.route.topics, topicToAdd) or _.isUndefined topicToAdd
			$scope.route.topics.push topicToAdd

	$scope.editTopic = (topicToEdit) ->
		$scope.route.topics = _.without $scope.route.topics, topicToEdit	
		$scope.topicToAdd = topicToEdit

	$scope.deleteTopic = (topicToDelete) ->
		$scope.route.topics = _.without $scope.route.topics, topicToDelete
		
	$scope.create = ->
		$http.put("/service/route", $scope.route)
			.success(-> $location.path "/routes")
			.error((data, status) -> alert("Could not create route!\nError: #{status}"))
			
	$scope.goBack = -> window.history.back()


window.RouteDeleteCtrl = ($scope, $http, $routeParams, $location) ->

	$scope.route = $http.get("/service/route/#{$routeParams.routeId}")
		.success((data) ->
			$scope.route = data)
	
	$scope.performDelete = ->
		$http.delete("/service/route/#{$routeParams.routeId}")
			.success(-> $location.path("/routes"))
			.error((e) -> alert("Could not delete route!"))
			
	$scope.goBack = ->
		window.history.back()



window.RouteHelpCtrl = ($scope) ->

	$scope.toggle = (e) ->

		$button = $("#" + e + " em")
		$content = $("#" + e + "_content")

		$button.toggleClass "icon-chevron-up"
		$button.toggleClass "icon-chevron-down"
		$content.slideToggle()

