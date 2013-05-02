package amp.topology.test;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import amp.bus.EnvelopeHelper;
import amp.topology.core.model.Client;

public class Utils {

	public static Map<String, String> getRoutingContext(String topic, String pattern){
		
		return new EnvelopeHelper()
		.setMessageTopic(topic)
		.setMessagePattern(pattern)
		.getEnvelope()
		.getHeaders();
	}
	
	public static Client getClient(String name, String...groups){
		
		return new Client(
			UUID.randomUUID().toString(), "desc", name, Arrays.asList(groups));
	}
}
