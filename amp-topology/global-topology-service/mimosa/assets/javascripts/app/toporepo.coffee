define [ "vendor/lodash", "app/exchange", "app/route", "config"  ], (_, Exchange, Route, config) ->
	
	class TopologyRepository
	
		@defaults = 
			routes:
				base: "../service"
				exchanges: "exchanges"
				routes: "routes"
	
		constructor: (overrides) ->
			overrides = overrides ? {}
			@config = TopologyRepository.defaults
			_.extend @config, overrides
		
		getExchange: (id, onSuccess, onError) =>
			console.log "Getting exchange with ID: #{id}"
			exchangeUrl = @_getBaseExchangeUrl()
			exchange = Exchange.createRecord({ isLoaded: false })
			handleSuccess = @_getSuccessDataAdaptor(onSuccess, exchange)
			@_callServer "#{exchangeUrl}exchange/#{id}", "GET", null, handleSuccess, onError
			return exchange
		
		getRoute: (id, onSuccess, onError) =>
			console.log "Getting route with ID: #{id}"
			routesUrl = @_getBaseRoutesUrl()
			route = Route.createRecord({ isLoaded: false })
			handleSuccess = @_getSuccessDataAdaptor(onSuccess, route)
			@_callServer "#{routesUrl}route/#{id}", "GET", null, handleSuccess, onError
			return route
		
		getExchanges: (onSuccess, onError) =>
			console.log "Getting exchanges."
			exchangeUrl = @_getBaseExchangeUrl()
			exchanges = Ember.ArrayController.create({ isLoaded: false, content: [] })
			handleSuccess = @_getSuccessDataArrayAdaptor(onSuccess, Exchange, exchanges)
			@_callServer "#{exchangeUrl}", "GET", null, handleSuccess, onError
			return exchanges
	
		getRoutes: (onSuccess, onError) =>
			console.log "Getting routes."
			routesUrl = @_getBaseRoutesUrl()
			routes = Ember.ArrayController.create({ isLoaded: false, content: [] })
			handleSuccess = @_getSuccessDataArrayAdaptor(onSuccess, Route, routes)
			@_callServer "#{routesUrl}", "GET", null, handleSuccess, onError
			return routes
	
		createExchange: (exchange, onSuccess, onError) =>
		
		
		createRoute: (route, onSuccess, onError) =>
		
		
		updateExchange: (exchange, onSuccess, onError) =>
		
		
		updateRoute: (route, onSuccess, onError) =>
		
		
		removeExchange: (id, onSuccess, onError) =>
		
		
		removeRoute: (id, onSuccess, onError) =>	
		
		
		_getBaseExchangeUrl: =>
			"#{@config.routes.base}/#{@config.routes.exchanges}/"
		
		_getBaseRoutesUrl: =>
			"#{@config.routes.base}/#{@config.routes.routes}/"
		
		_getSuccessDataAdaptor: (onSuccess, modelObject) =>
			(data) ->
				modelObject.setProperties(data)
				modelObject.set("isLoaded", true)
				onSuccess(modelObject) if onSuccess?
		
		_getSuccessDataArrayAdaptor: (onSuccess, modelClass, arrayController) =>
			(data) ->
				for item in data
					modelObject = modelClass.createRecord({ isLoaded: false })
					console.log modelObject
					modelObject.setProperties(item)
					modelObject.set("isLoaded", true)
					arrayController.addObject modelObject
				arrayController.set("isLoaded", true)
				onSuccess(dataArray) if onSuccess?
		
		_callServer: (url, method, payload, onSuccess, onError) =>
			settings =
				dataType: "json"
				type: method
				error: onError ? (req, status, error) -> console.error error
				success: onSuccess
			settings.data = payload if payload
			$.ajax url, settings
		
	return new TopologyRepository(config)
	