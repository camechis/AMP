package amp.topology.core.repo;

import java.util.Collection;

import amp.topology.core.model.ClusterDoesntExistException;

import rabbitmq.mgmt.model.ExchangeType;
import rabbitmq.mgmt.model.ListenerContext;
import rabbitmq.mgmt.model.Node;
import rabbitmq.mgmt.model.Overview;

public interface ClusterInfoRepository {

	Overview getOverview(String clusterId) throws ClusterDoesntExistException;
	
	Collection<ExchangeType> getExchangeTypes(String clusterId) throws ClusterDoesntExistException;
	
	Collection<Node> getNodes(String clusterId) throws ClusterDoesntExistException;
	
	Node getNode(String clusterId, String name) throws ClusterDoesntExistException;
	
	Collection<ListenerContext> getListenerEndpoints(String clusterId) throws ClusterDoesntExistException;
}