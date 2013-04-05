

window.TopoGraphCtrl = ($scope, $http, $route, $location) ->

	LABEL_COLOR    = "#333"
	EXCHANGE_COLOR = "#DD5600"
	EXCHANGE_SIZE  = 3
	ROUTE_COLOR    = "#2FA4E7"
	ROUTE_SIZE     = 5
	BROKER_COLOR   = "#9760B3"
	BROKER_SIZE    = 2
	CLIENT_COLOR   = "#73A839"
	CLIENT_SIZE    = 1
	TOPIC_COLOR    = "#C71C22"
	TOPIC_SIZE     = 1
	
	sigInst = sigma.init($("#topo-canvas")[0])
	
	drawProps = 
		labelColor: "node"
		defaultLabelColor: LABEL_COLOR,
		defaultLabelSize: 14,
		defaultLabelBGColor: '#000',
		defaultLabelHoverColor: '#000',
		labelThreshold: 6,
		defaultEdgeType: 'curve'
	
	graphProps = 
		minNodeSize: 5,
		maxNodeSize: 15,
		minEdgeSize: 3,
		maxEdgeSize: 5
	
	sigInst.drawingProperties(drawProps).graphProperties(graphProps)
	
	getOps = (description, color, size) ->
		exchangeOps = 
			label: description
			x: Math.random()
			y: Math.random()
			color: color
			size: size
			originalColor: color
			forceLabel: true
	
	drawExchange = (exchange) ->
		exchangeOps = getOps exchange.description, EXCHANGE_COLOR, EXCHANGE_SIZE
		sigInst.addNode("/exchanges/" + exchange.id, exchangeOps)
	
	knownBrokers = []
	
	drawBroker = (broker) ->
		unless _.contains knownBrokers, broker
			brokerOps = getOps broker, BROKER_COLOR, BROKER_SIZE
			sigInst.addNode("/brokers/" + broker, brokerOps)
			knownBrokers.push broker
	
	drawRoute = (route) ->
		routeOps = getOps route.description, ROUTE_COLOR, ROUTE_SIZE
		sigInst.addNode("/routes/" + route.id, routeOps)
	
	knownClients = []
	
	drawClient = (client) ->
		unless _.contains knownClients, client
			clientOps = getOps client, CLIENT_COLOR, CLIENT_SIZE
			sigInst.addNode("/clients/" + client, clientOps)
			knownClients.push client
	
	knownTopics = []
	
	drawTopic = (topic) ->
		unless _.contains knownTopics, topic
			topicOps = getOps topic, TOPIC_COLOR, TOPIC_SIZE
			sigInst.addNode("/topics/" + topic, topicOps)
			knownTopics.push topic
	
	drawEdge = (v1, v2, color, size, prefix) ->
		prefix = prefix ? ""
		edgeOps = 
			color: color
			originalColor: color
			size: size ? 2
		sigInst.addEdge("#{prefix}#{v1}_#{v2}", v1, v2, edgeOps)
	
	async.parallel([
		((callback) -> 
			$http.get("/service/exchange").success (exchanges) ->
				callback(null, exchanges)
		),
		((callback) -> 
			$http.get("/service/route").success (routes) ->
				callback(null, routes)
		),
	], (err, results) -> 
		for exchange in results[0]
			drawExchange(exchange)
			brokerId = "amqp://#{exchange.hostName}:#{exchange.port}#{exchange.virtualHost}"
			drawBroker(brokerId)
			drawEdge("/exchanges/" + exchange.id, "/brokers/" + brokerId, BROKER_COLOR)
		for route in results[1]
			drawRoute(route)
			for client in route.clients
				drawClient(client)
				drawEdge("/routes/" + route.id, "/clients/" + client, CLIENT_COLOR)
			for topic in route.topics
				drawTopic(topic)
				drawEdge("/routes/" + route.id, "/topics/" + topic, TOPIC_COLOR)
			drawEdge("/routes/" + route.id, "/exchanges/" + route.producerExchangeId, EXCHANGE_COLOR, 4, "p_")
			drawEdge("/routes/" + route.id, "/exchanges/" + route.consumerExchangeId, EXCHANGE_COLOR, 4, "c_")
		sigInst.draw()
		
		sigInst.startForceAtlas2()
		
		$tc = $("#topo-canvas")
		
		$tc.mouseover -> 
			sigInst.stopForceAtlas2()
			sigInst.draw()
			
		$tc.mouseout ->
			sigInst.startForceAtlas2()
		
		threshold = 1000
		lastClick = 0
		latch = 0
		target = null
		
		doubleClickHandler = (node) ->
			currentTime = new Date().getTime()
			testnode = node.content[0]
			if target is testnode and (currentTime - lastClick) <= threshold
				if latch is 1
					$location.path(node.content[0])
					$scope.$apply()
			else
				lastClick = currentTime
				latch = 1
				target = node.content[0]
			
		
		sigInst.bind("downnodes", doubleClickHandler)
		
		sigInst.bind('overnodes', (event)-> 
			targetNode = event.content[0]
			connectedNodes = [ targetNode ]
			sigInst.iterEdges (edge) ->
				unless edge.target is targetNode or edge.source is targetNode
					edge.color = "#ccc"
				else
					if edge.target is targetNode
						connectedNodes.push edge.source
					else
						connectedNodes.push edge.target
			sigInst.iterNodes (node) ->
				unless _.contains connectedNodes, node.id
					node.color = "#ccc"
			
			sigInst.draw()
		)
		
		sigInst.bind('outnodes', (event)-> 
			sigInst.iterNodes (node) ->
				node.color = node.attr.originalColor
			sigInst.iterEdges (edge) ->
				edge.color = edge.attr.originalColor
			sigInst.draw()
		)
		
		
			
	)
	