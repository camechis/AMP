package amp.topology.core.model.definitions;

import java.util.ArrayList;
import java.util.Collection;

import amp.bus.rabbit.topology.Exchange;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.RoutingContext;
import amp.topology.core.model.TopologyModel;

public abstract class BaseRouteDefinition extends TopologyModel implements TopologyDefinition {

	protected FactoryReference<Cluster> clusterReference;
	protected FactoryReference<Exchange> exchangeReference;
	protected ArrayList<FactoryReference<RoutingContext>> routingContextReferences = new ArrayList<FactoryReference<RoutingContext>>();
	
	public BaseRouteDefinition(){}
	
	public BaseRouteDefinition(String id, String description,
			FactoryReference<Cluster> clusterReference,
			FactoryReference<Exchange> exchangeReference,
			ArrayList<FactoryReference<RoutingContext>> routingKeys) {
		
		super(id, description);
		this.clusterReference = clusterReference;
		this.exchangeReference = exchangeReference;
		this.routingContextReferences = routingKeys;
	}
	
	public FactoryReference<Cluster> getClusterReference() {
		return clusterReference;
	}

	public FactoryReference<Exchange> getExchangeReference() {
		return exchangeReference;
	}

	public Collection<FactoryReference<RoutingContext>> getRoutingContextReferences() {
		return routingContextReferences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((clusterReference == null) ? 0 : clusterReference.hashCode());
		result = prime
				* result
				+ ((exchangeReference == null) ? 0 : exchangeReference
						.hashCode());
		result = prime * result
				+ ((routingContextReferences == null) ? 0 : routingContextReferences.hashCode());
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
		BaseRouteDefinition other = (BaseRouteDefinition) obj;
		if (clusterReference == null) {
			if (other.clusterReference != null)
				return false;
		} else if (!clusterReference.equals(other.clusterReference))
			return false;
		if (exchangeReference == null) {
			if (other.exchangeReference != null)
				return false;
		} else if (!exchangeReference.equals(other.exchangeReference))
			return false;
		if (routingContextReferences == null) {
			if (other.routingContextReferences != null)
				return false;
		} else if (!routingContextReferences.equals(other.routingContextReferences))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BaseRouteDefinition [clusterReference=" + clusterReference
				+ ", exchangeReference=" + exchangeReference + ", routingContextReferences="
				+ routingContextReferences + ", id=" + id + ", description=" + description
				+ "]";
	}
}
