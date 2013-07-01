package amp.topology.core;

public class TopologyRequestContext {

	private String client;
	private String topic;
	private String queuePrefix;
	private String queueName;
	
	
	public TopologyRequestContext(
			String client, 
			String topic,
			String queuePrefix, 
			String queueName) {
		
		this.client = client;
		this.topic = topic;
		this.queuePrefix = queuePrefix;
		this.queueName = queueName;
	}

	public String getClient() {
		return client;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public String getQueuePrefix() {
		return queuePrefix;
	}
	
	public String getQueueName() {
		return queueName;
	}
}
