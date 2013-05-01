package amp.topology.core.model;

public class MessagingPattern extends TopologyModel {

	private String pattern;
	
	public MessagingPattern(){}
	
	public MessagingPattern(String id, String description, String pattern) {
		super(id, description);
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
	
}