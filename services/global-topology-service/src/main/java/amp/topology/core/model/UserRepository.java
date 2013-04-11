package amp.topology.core.model;

import java.util.Collection;

import rabbitmq.mgmt.model.Permission;
import rabbitmq.mgmt.model.User;

public interface UserRepository {

	void create(String clusterId, User user) throws ClusterDoesntExistException;
	
	void delete(String clusterId, String username) throws ClusterDoesntExistException;
	
	User get(String clusterId, String username) throws ClusterDoesntExistException;
	
	Collection<User> all(String clusterId) throws ClusterDoesntExistException;
	
	Collection<Permission> getPermissions(String clusterId, String username) throws ClusterDoesntExistException;

	void setPermission(String clusterId, Permission permission) throws ClusterDoesntExistException;
	
	void removePermission(String clusterId, final String vhost, final String username) throws ClusterDoesntExistException;
}