
window.TopicListCtrl = ($scope, $http, $route, $location) ->
	
	$scope.topics = $http.get("/service/route/topics")
		.success (data) ->
			$scope.topics = data


window.TopicViewCtrl = ($scope, $http, $location, $routeParams) ->
	
	$scope.topicId = $routeParams.topicId
	$scope.routes = $http.get("/service/route/topic/#{$routeParams.topicId}")
		.success (data) ->
			$scope.routes = data