angular.module("gts", ["ui", "ui.bootstrap", "filters"])
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
			# TOPICS
			.when("/topics", 
				{ templateUrl: "partials/topic-list.html", controller: TopicListCtrl })
			.when("/topics/:topicId", 
				{ templateUrl: "partials/topic-view.html", controller: TopicViewCtrl })
			# CLIENTS
			.when("/clients", 
				{ templateUrl: "partials/client-list.html", controller: ClientListCtrl })
			.when("/clients/:clientId", 
				{ templateUrl: "partials/client-view.html", controller: ClientViewCtrl })
			# SNAPSHOTS
			.when("/snapshots", 
				{ templateUrl: "partials/snapshot-list.html", controller: SnapshotListCtrl })
			.when("/snapshots/:snapshotId", 
				{ templateUrl: "partials/snapshot-view.html", controller: SnapshotViewCtrl })
			.when("/snapshots/:snapshotId/clone", 
				{ templateUrl: "partials/snapshot-clone.html", controller: SnapshotCloneCtrl })
			# DEFAULT
			.when("/", 
				{ templateUrl: "partials/main.html", controller: MainCtrl })
			.otherwise({ redirectTo: "/" })
	])
		