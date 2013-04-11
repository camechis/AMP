package amp.topology.core.model;

import java.util.Collection;

import rabbitmq.mgmt.model.Permission;
import rabbitmq.mgmt.model.Status;
import rabbitmq.mgmt.model.VirtualHost;

public interface VirtualHostRepository {

	
	Collection<VirtualHost> getVirtualHosts(String clusterId) throws ClusterDoesntExistException;
	
	Status getStatus(String clusterId, String vhost) throws ClusterDoesntExistException;
	
	Collection<Permission> getPermissions(String clusterId, String vhost) throws ClusterDoesntExistException;
	
	void setPermission(String clusterId, Permission permission) throws ClusterDoesntExistException;
	
	void removePermission(String clusterId, String vhost, String username) throws ClusterDoesntExistException;
}