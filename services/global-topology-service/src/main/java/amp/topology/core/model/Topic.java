package amp.topology.core.model;

public class Topic extends TopologyModel {
	
	public Topic(){}
	
	public Topic(String id, String description) {
		super(id, description);
	}
	
	@Override
	public String toString() {
		return "Topic [id=" + id + ", description=" + description + "]";
	}
}