package amp.topology.client;

import java.util.Map;

public class RoutingInfoNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 7893984310165704345L;

	public RoutingInfoNotFoundException(Map<String, String> routingHints){
		
		super("RoutingInfo was not found for hints: " + routingHints);
	}
}