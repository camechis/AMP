define [ "vendor/lodash", "vendor/ember-data", "config"  ], (_, ed, config) ->
	
	return DS.Model.extend({
		description: DS.attr("string"),
		name: DS.attr("string"),
		hostName: DS.attr("string"),
		virtualHost: DS.attr("string"),
		port: DS.attr("number"),
		routingKey: DS.attr("string"),
		queueName: DS.attr("string"),
		exchangeType: DS.attr("string"),
		isDurable: DS.attr("boolean"),
		isAutoDelete: DS.attr("boolean"),
		arguments: DS.attr("array"),
	})