package amp.topology.core.model.definitions;

import java.util.ArrayList;

import amp.bus.rabbit.topology.Exchange;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.RoutingContext;

public class ProducingRouteDefinition extends BaseRouteDefinition {
	
	public ProducingRouteDefinition() { super(); }

	public ProducingRouteDefinition(String id, String description,
			FactoryReference<Cluster> clusterReference,
			FactoryReference<Exchange> exchangeReference,
			ArrayList<FactoryReference<RoutingContext>> routingKeys) {
		
		super(id, description, clusterReference, exchangeReference, routingKeys);
	}

	@Override
	public String toString() {
		return "ProducingRouteDefinition [clusterReference=" + clusterReference
				+ ", exchangeReference=" + exchangeReference + ", routingKeys="
				+ routingContextReferences + ", id=" + id + ", description=" + description
				+ "]";
	}
}
