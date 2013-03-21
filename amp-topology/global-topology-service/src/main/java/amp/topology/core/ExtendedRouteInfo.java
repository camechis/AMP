package amp.topology.core;

import java.util.List;
import java.util.UUID;

public class ExtendedRouteInfo {
	
	private String id;
	private String description;
	private List<String> clients;
	private List<String> topics;
	private String consumerExchangeId;
	private String producerExchangeId;
	
	public ExtendedRouteInfo(){}
	
	public ExtendedRouteInfo(String description,
			List<String> clients, List<String> topics,
			String consumerExchangeId, String producerExchangeId) {
		
		this.id = UUID.randomUUID().toString();
		this.description = description;
		this.clients = clients;
		this.topics = topics;
		this.consumerExchangeId = consumerExchangeId;
		this.producerExchangeId = producerExchangeId;
	}
	
	public ExtendedRouteInfo(String id, String description,
			List<String> clients, List<String> topics,
			String consumerExchangeId, String producerExchangeId) {
		this.id = id;
		this.description = description;
		this.clients = clients;
		this.topics = topics;
		this.consumerExchangeId = consumerExchangeId;
		this.producerExchangeId = producerExchangeId;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
	
	public List<String> getClients() {
		return clients;
	}

	public List<String> getTopics() {
		return topics;
	}
	
	public String getConsumerExchangeId() {
		return consumerExchangeId;
	}

	public String getProducerExchangeId() {
		return producerExchangeId;
	}
}
