package amp.topology.core.model.definitions;

import amp.topology.core.model.TopologyModel;

public class RoutingContextDefinition extends TopologyModel implements TopologyDefinition {

	protected String valueExpression;
	
	public RoutingContextDefinition(){}

	public RoutingContextDefinition(String id, String description,
			String valueExpression) {
		
		super(id, description);
		
		this.valueExpression = valueExpression;
	}

	public String getValueExpression() {
		return valueExpression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((valueExpression == null) ? 0 : valueExpression.hashCode());
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
		RoutingContextDefinition other = (RoutingContextDefinition) obj;
		if (valueExpression == null) {
			if (other.valueExpression != null)
				return false;
		} else if (!valueExpression.equals(other.valueExpression))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoutingContextDefinition [valueExpression=" + valueExpression
				+ ", id=" + id + ", description=" + description + "]";
	}
}
