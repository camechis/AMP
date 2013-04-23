package amp.topology.core.model;

public class RoutingContext extends TopologyModel {

	protected String value;

	public RoutingContext(){}
	
	public RoutingContext(String id, String description, String value) {
		super(id, description);
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "RoutingContext [value=" + value + ", id=" + id + ", description="
				+ description + "]";
	}
}
