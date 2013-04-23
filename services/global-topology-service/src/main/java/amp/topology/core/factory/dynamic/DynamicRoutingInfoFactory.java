package amp.topology.core.factory.dynamic;

import java.util.Collection;

import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;
import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.factory.impl.BaseChainedTopologyFactory;
import amp.topology.core.model.definitions.RoutingInfoDefinition;

public class DynamicRoutingInfoFactory extends BaseChainedTopologyFactory<RoutingInfo> {

	DefinitionRepository<RoutingInfoDefinition> repository;
	
	public DynamicRoutingInfoFactory(DefinitionRepository<RoutingInfoDefinition> repository){
		
		this.repository = repository;
	}

	@Override
	public RoutingInfo build(String context) {
		
		RoutingInfoDefinition definition = this.repository.get(context);
		
		Collection<ProducingRoute> proutes = 
			constructAggregates(definition.getProducingRouteReferences());
		
		Collection<ConsumingRoute> croutes = 
			constructAggregates(definition.getConsumingRouteReferences());
		
		return new RoutingInfo(proutes, croutes);
	}
	
	
	
}
