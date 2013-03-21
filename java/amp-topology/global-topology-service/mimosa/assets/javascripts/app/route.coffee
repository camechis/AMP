define [ "vendor/lodash", "vendor/ember-data", "config"  ], (_, ed, config) ->
	
	return DS.Model.extend({
		description: DS.attr("string")
		consumerExchangeId: DS.attr("string")
		producerExchangeId: DS.attr("string")
		clients: DS.attr("array")
		topics: DS.attr("array")
	})