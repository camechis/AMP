package amp.topology.core.model.definitions;

import java.util.ArrayList;
import java.util.Collection;

import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.TopologyModel;

public class RoutingInfoDefinition extends TopologyModel implements TopologyDefinition {
	
	/**
	 * And if the search matches, we provide these routes.
	 */
	protected ArrayList<FactoryReference<ProducingRoute>> producingRouteReferences = 
			new ArrayList<FactoryReference<ProducingRoute>>();
	
	protected ArrayList<FactoryReference<ConsumingRoute>> consumingRouteReferences = 
			new ArrayList<FactoryReference<ConsumingRoute>>();
	
	public RoutingInfoDefinition(){}
	
	public RoutingInfoDefinition(
			String id, String description,
			Collection<FactoryReference<ProducingRoute>> producingRouteReferences,
			Collection<FactoryReference<ConsumingRoute>> consumingRouteReferences) {
		
		super(id, description);
		
		this.producingRouteReferences.addAll(producingRouteReferences);
		this.consumingRouteReferences.addAll(consumingRouteReferences);
	}

	public Collection<FactoryReference<ProducingRoute>> getProducingRouteReferences() {
		return producingRouteReferences;
	}

	public Collection<FactoryReference<ConsumingRoute>> getConsumingRouteReferences() {
		return consumingRouteReferences;
	}
	
}