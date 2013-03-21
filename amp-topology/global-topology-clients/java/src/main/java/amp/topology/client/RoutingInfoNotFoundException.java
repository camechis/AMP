package amp.topology.client;

public class RoutingInfoNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 7893984310165704345L;

	public RoutingInfoNotFoundException(String topic){
		
		super("RoutingInfo was not found for topic: " + topic);
	}
}