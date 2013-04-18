package amp.topology.core.model;

import java.util.Collection;

import rabbitmq.mgmt.model.Binding;

public interface BindingRepository {

	Collection<Binding> get(String clusterId);
	
	Collection<Binding> get(String clusterId, String vhost);
	
	Collection<Binding> get(String clusterId, String vhost, String source, String destination);
	
	Binding get(String clusterId, String vhost, String source, String destination, String routingkey);
	
	void createBinding(String clusterId, Binding binding) throws ClusterDoesntExistException;
	
	void removeBinding(String clusterId, Binding binding) throws ClusterDoesntExistException;
}
