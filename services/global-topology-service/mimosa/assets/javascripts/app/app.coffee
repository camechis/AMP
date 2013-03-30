angular.module("gts", ["filters"])
	.config(["$routeProvider", ($routeProvider) ->
		$routeProvider
			# EXCHANGES
			.when("/exchanges", 
				{ templateUrl: "partials/exchange-list.html", controller: ExchangeListCtrl })
			.when("/exchanges/:exchangeId", 
				{ templateUrl: "partials/exchange-view.html", controller: ExchangeViewCtrl })
			.when("/exchanges/:exchangeId/edit", 
				{ templateUrl: "partials/exchange-edit.html", controller: ExchangeEditCtrl })
			.when("/exchanges/:exchangeId/delete", 
				{ templateUrl: "partials/exchange-delete.html", controller: ExchangeDeleteCtrl })
			.when("/exchange/new", 
				{ templateUrl: "partials/exchange-edit.html", controller: ExchangeNewCtrl })
			# ROUTES
			.when("/routes", 
				{ templateUrl: "partials/route-list.html", controller: RouteListCtrl })
			.when("/routes/:routeId", 
				{ templateUrl: "partials/route-view.html", controller: RouteViewCtrl })
			.when("/routes/:routeId/edit", 
				{ templateUrl: "partials/route-edit.html", controller: RouteEditCtrl })
			.when("/routes/:routeId/delete", 
				{ templateUrl: "partials/route-delete.html", controller: RouteDeleteCtrl })
			.when("/route/new", 
				{ templateUrl: "partials/route-edit.html", controller: RouteNewCtrl })
			.otherwise({ redirectTo: "/exchanges" })
	])
		