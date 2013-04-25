package amp.topology.core.factory.dynamic;

import java.util.Collection;

import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.Queue;
import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.RoutingContext;
import amp.topology.core.model.definitions.ConsumingRouteDefinition;

public class DynamicConsumingRouteFactory extends EvaluatingChainedTopologyFactory<ConsumingRoute> {

	DefinitionRepository<ConsumingRouteDefinition> repository;
	
	public DynamicConsumingRouteFactory(
			ExpressionEvaluator evaluator,
			DefinitionRepository<ConsumingRouteDefinition> repository) {
		
		super(evaluator);
		
		this.repository = repository;
	}
	
	@Override
	public ConsumingRoute build(String context) {
		
		ConsumingRouteDefinition definition = this.repository.get(context);
		
		Exchange exchange = this.constructAggregate(definition.getExchangeReference());
		
		Queue queue = this.constructAggregate(definition.getQueueReference());
		
		Cluster cluster = this.constructAggregate(definition.getClusterReference());
		
		Collection<RoutingContext> routingContexts = 
				this.constructAggregates(definition.getRoutingContextReferences());
		
		return ConsumingRoute.builder()
				.exchange(exchange)
				.queue(queue)
				.brokers(cluster.getBrokers())
				.routingkeys( Utils.asRoutingKeys(routingContexts, this.evaluator) )
				.build();
	}

	
	
}
