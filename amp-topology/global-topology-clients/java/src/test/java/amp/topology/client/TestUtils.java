package amp.topology.client;

import java.util.HashMap;
import java.util.Map;

import cmf.bus.EnvelopeHeaderConstants;

public class TestUtils {

	public static Map<String, String> buildRoutingHints(String topic){
		
		Map<String, String> routingHints = new HashMap<String, String>();
		
		routingHints.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, topic);
		
		return routingHints;
	}
	
}
