
window.ExchangeListCtrl = ($scope, $http, $route, $location) ->
	
	$scope.exchanges = $http.get("/service/exchange")
		.success (data) ->
			$scope.exchanges = data
			$scope.brokerCount = _.size(_.groupBy data, (datum) -> datum.hostName)
	
	$scope.delete = (exchangeId) ->
		$location.path "/exchanges/#{exchangeId}/delete"
	
	$scope.edit = (exchangeId) ->
		$location.path "/exchanges/#{exchangeId}/edit"
	
	$scope.create = ->
		$location.path "/exchange/new"
		
	$scope.refresh = ->
		$route.reload()


window.ExchangeViewCtrl = ($scope, $http, $routeParams, $location) ->
	
	$scope.exchange = $http.get("/service/exchange/#{$routeParams.exchangeId}")
		.success((data) ->
			$scope.exchange = data)
			
	$scope.goto = (route) ->
		$location.path route


window.ExchangeEditCtrl = ($scope, $http, $routeParams, $location) ->
	
	$scope.exchange = $http.get("/service/exchange/#{$routeParams.exchangeId}")
		.success((data) ->
			$scope.exchange = data
			$scope.action = "edit")
	
	$scope.update = ->
		$http.post("/service/exchange/#{$scope.exchange.id}", $scope.exchange)
			.success(-> $location.path "/exchanges")
			.error((data, status) -> alert("Could not update exchange!\nError: #{status}"))
	
	$scope.goBack = -> window.history.back()
	

window.ExchangeNewCtrl = ($scope, $http, $routeParams, $location) ->

	$scope.exchange = 
		arguments: null
		exchangeType: "topic"
		hostName: ""
		isAutoDelete: true
		isDurable: true
		name: ""
		port: 5672
		queueName: ""
		routingKey: ""
		virtualHost: "/"
		id: uuid.v4()
		description: ""
		
	$scope.action = "new"
	
	$scope.create = ->
		$http.put("/service/exchange", $scope.exchange)
			.success(-> $location.path "/exchanges")
			.error((data, status) -> alert("Could not create exchange!\nError: #{status}"))
		
	$scope.goBack = -> window.history.back()

window.ExchangeDeleteCtrl = ($scope, $http, $routeParams, $location) ->

	$scope.exchange = $http.get("/service/exchange/#{$routeParams.exchangeId}")
		.success((data) ->
			$scope.exchange = data)
	
	$scope.performDelete = ->
		$http.delete("/service/exchange/#{$routeParams.exchangeId}")
			.success(-> $location.path "/exchanges")
			.error((data, status) -> alert("Could not delete exchange! Error: #{status}"))
			
	$scope.goBack = -> window.history.back()
		
window.ExchangeHelpCtrl = ($scope) ->
	
	$scope.toggle = (e) ->
		
		$button = $("#" + e + " em")
		$content = $("#" + e + "_content")
			
		$button.toggleClass "icon-chevron-up"
		$button.toggleClass "icon-chevron-down"
		$content.slideToggle()
		
		