package amp.topology.core.model.definitions;

import java.util.ArrayList;

import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.Queue;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.RoutingContext;

public class ConsumingRouteDefinition extends BaseRouteDefinition {
	
	protected FactoryReference<Queue> queueReference;
	
	public ConsumingRouteDefinition(){ super(); }
	
	public ConsumingRouteDefinition(String id, String description,
			FactoryReference<Cluster> clusterReference,
			FactoryReference<Exchange> exchangeReference,
			ArrayList<FactoryReference<RoutingContext>> routingKeys,
			FactoryReference<Queue> queueReference) {
		
		super(id, description, clusterReference, exchangeReference, routingKeys);
		
		this.queueReference = queueReference;
	}
	
	public FactoryReference<Queue> getQueueReference() {
		return queueReference;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((queueReference == null) ? 0 : queueReference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsumingRouteDefinition other = (ConsumingRouteDefinition) obj;
		if (queueReference == null) {
			if (other.queueReference != null)
				return false;
		} else if (!queueReference.equals(other.queueReference))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConsumingRouteDefinition [queueReference=" + queueReference
				+ ", clusterReference=" + clusterReference
				+ ", exchangeReference=" + exchangeReference + ", routingKeys="
				+ routingContextReferences + ", id=" + id + ", description=" + description
				+ "]";
	}
}