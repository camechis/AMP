package amp.topology.test;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import amp.bus.EnvelopeHelper;
import amp.topology.core.model.Client;
import amp.topology.core.model.MessagingPattern;
import amp.topology.core.model.Topic;

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
	
	public static Topic topic(String topic){
		
		return new Topic(UUID.randomUUID().toString(), "desc", topic);
	}
	
	public static MessagingPattern pattern(String pattern){
		
		return new MessagingPattern(UUID.randomUUID().toString(), "desc", pattern);
	}
}
