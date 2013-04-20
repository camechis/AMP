package amp.topology.core.repo;

import java.util.Collection;

import amp.topology.core.model.ClusterDoesntExistException;

import rabbitmq.mgmt.model.Exchange;

public interface ExchangeRepository {

	void create(String clusterId, Exchange exchange) throws ClusterDoesntExistException;
	
	void delete(String clusterId, String vhost, String exchangeName) throws ClusterDoesntExistException;
	
	Exchange get(String clusterId, String vhost, String exchagneName) throws ClusterDoesntExistException;
	
	Collection<Exchange> all(String clusterId) throws ClusterDoesntExistException;
	
	Collection<Exchange> all(String clusterId, String vhost) throws ClusterDoesntExistException;
}
