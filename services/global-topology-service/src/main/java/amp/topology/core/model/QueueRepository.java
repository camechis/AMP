package amp.topology.core.model;

import java.util.Collection;

import rabbitmq.mgmt.model.Queue;

public interface QueueRepository {

	void create(String clusterId, Queue queue) throws ClusterDoesntExistException;
	
	void delete(String clusterId, String vhost, String queueName) throws ClusterDoesntExistException;
	
	Queue get(String clusterId, String vhost, String queueName) throws ClusterDoesntExistException;
	
	Collection<Queue> all(String clusterId) throws ClusterDoesntExistException;
	
	Collection<Queue> all(String clusterId, String vhost) throws ClusterDoesntExistException;
}
