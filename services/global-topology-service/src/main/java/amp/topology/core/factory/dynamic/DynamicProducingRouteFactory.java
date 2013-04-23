package amp.topology.core.factory.dynamic;

import java.util.Collection;

import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.ProducingRoute;
import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.factory.impl.BaseChainedTopologyFactory;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.RoutingContext;
import amp.topology.core.model.definitions.ProducingRouteDefinition;

public class DynamicProducingRouteFactory extends BaseChainedTopologyFactory<ProducingRoute> {

	DefinitionRepository<ProducingRouteDefinition> repository;
	
	public DynamicProducingRouteFactory(
		DefinitionRepository<ProducingRouteDefinition> repository){
		
		this.repository = repository;
	}
	
	@Override
	public ProducingRoute build(String context) {
		
		ProducingRouteDefinition definition = this.repository.get(context);
		
		Exchange exchange = this.constructAggregate(definition.getExchangeReference());
		
		Cluster cluster = this.constructAggregate(definition.getClusterReference());
		
		Collection<RoutingContext> routingContexts = 
				this.constructAggregates(definition.getRoutingContextReferences());
		
		return ProducingRoute.builder()
				.brokers( cluster.getBrokers() )
				.exchange( exchange )
				.routingKeys( Utils.asRoutingKeys( routingContexts ) )
				.build();
	}
	
	
	
}
