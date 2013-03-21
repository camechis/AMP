dependencies = [ 
	"vendor/jquery", "vendor/handlebars.1.0.rc.2", 
	"vendor/ember", "vendor/bootstrap", 
	"text!app/templates.html",
	"app/toporepo", "app/route", "app/exchange"  ]

require dependencies, (jq, h, e, bs, templates, topoRepository, RouteInfo, Exchange) ->
	
	Ember.Handlebars.bootstrap($(templates))
	
	window.App = Ember.Application.create({
		LOG_TRANSITIONS: true
		currentPath: ""
		ApplicationController: Ember.Controller.extend({
			updateCurrentPath: (->
				App.set("currentPath", @get("currentPath"))
			).observes("currentPath")
		})
	})
	
	App.RouteInfo = RouteInfo
	App.Exchange = Exchange
	
	App.MenuItemView = Ember.View.extend({
		tagName: "li"
		classNameBindings: ["active"]
		didInsertElement: ->
			@_super();
			_this = @
			@get('parentView').on 'click', -> 
				_this.notifyPropertyChange('active')
		active: ( -> 
			@get("childViews.firstObject.active")
		).property()
	})
	
	App.Router.map ->
		this.resource "status"
		this.resource "routeInfos", ->
			this.resource "routeInfo", { path: "/:routeInfo_id" }
		this.resource "exchanges", ->
			this.resource "exchange", { path: "/:exchange_id" }
		this.resource "settings"
		this.resource "history"
	
	App.StatusRoute = Ember.Route.extend({
		
	})
	
	App.RouteInfosRoute = Ember.Route.extend({
		model: -> App.RouteInfo.find()
	})
	
	App.RouteInfoRoute = Ember.Route.extend({
		model: (model) -> App.RouteInfo.find(model.route_id)
	})
	
	App.ExchangesRoute = Ember.Route.extend({
		model: (model) -> App.Exchange.find()
	})
	
	App.ExchangeRoute = Ember.Route.extend({
		model: (model) -> App.Exchange.find(model.exchange_id)
	})
	
	DS.JSONTransforms.array =
	  serialize: (jsonData)->
	    if Em.typeOf(jsonData) is 'array' then jsonData else []
	  deserialize: (externalData)->
	    switch Em.typeOf(externalData)
	      when 'array'  then return externalData
	      when 'string' then return externalData.split(',').map((item)-> jQuery.trim(item))
	      else               return []
	
	App.RESTAdapter = DS.RESTAdapter.extend()
	
	App.RESTAdapter.configure('plurals', {
		routeInfo: "routeInfo"
		exchange: "exchange"
	})
	
	App.RESTAdapter.configure('App.RouteInfo', {
		sideloadAs: 'routeInfo' 
	})
	
	App.RESTAdapter.map('App.RouteInfo', {
		routeInfo: {'embedded': 'load'}
	});
	
	App.RESTAdapter.configure('App.Exchange', {
		sideloadAs: 'exchange' 
	})
	
	App.RESTAdapter.map("App.Exchange", {
		hostName: { key: "hostName" }
		virtualHost: { key: "virtualHost" }
		queueName: { key: "queueName" }
		routingKey: { key: "routingKey" }
		exchangeType: { key: "exchangeType" }
		isDurable: { key: "isDurable" }
		isAutoDelete : { key: "isAutoDelete" }
	})
	
	App.Store = DS.Store.extend({
		revision: 11
		adapter: App.RESTAdapter.create({
			namespace: "service"
			bulkCommits: false
		})
	})
	
	
	
	
	
	