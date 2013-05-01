package amp.topology.core.model;

public class Topic extends TopologyModel {
	
	private String name;

	public Topic(){}
	
	public Topic(String id, String description, String name) {
		super(id, description);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}