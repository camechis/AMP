package amp.topology.rmq.repo.impl;

import java.util.Collection;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Permission;
import rabbitmq.mgmt.model.User;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.rmq.repo.UserRepository;

public class RmqUserRepository extends RmqBaseRepository implements UserRepository {
	
	public RmqUserRepository(RmqManagementFactory managementFactory) {
		super(managementFactory);
	}

	@Override
	public void create(String clusterId, final User user)
			throws ClusterDoesntExistException {
		
		logger.debug("Creating user with name {} on cluster {}.", user.getName(), clusterId);
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.users().create(user);
			}
		});
	}

	@Override
	public void delete(String clusterId, final String username)
			throws ClusterDoesntExistException {
		
		logger.debug("Deleting user with name {} on cluster {}.", username, clusterId);
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.users().delete(username);
			}
		});
	}

	@Override
	public User get(String clusterId, final String username)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting user with name {} on cluster {}.", username, clusterId);
		
		return this.execute(clusterId, new ManagementFunction<User>(){

			@Override
			public User perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.users().get(username);
			}
		});
	}

	@Override
	public Collection<User> all(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting all users on cluster {}.", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<User>>(){

			@Override
			public Collection<User> perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.users().all();
			}
		});
	}

	
	
	@Override
	public Collection<Permission> getPermissions(String clusterId, final String username) 
			throws ClusterDoesntExistException {
		
		logger.debug("Getting permissions for user with name {} on cluster {}.", username, clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Permission>>(){

			@Override
			public Collection<Permission> perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.users().permissionsFor(username);
			}
		});
	}

	@Override
	public void setPermission(String clusterId, final Permission permission)
			throws ClusterDoesntExistException {
		
		logger.debug("Setting permission for user with name {} on cluster {} and vhost {}.", 
			new Object[]{ permission.getUser(), clusterId, permission.getVhost() });
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.permissions().set(permission);
			}
		});
	}

	@Override
	public void removePermission(String clusterId, final String vhost, final String username)
			throws ClusterDoesntExistException {
		
		logger.debug("Removing permission for user with name {} on cluster {} and vhost {}.", 
			new Object[]{ username, clusterId, vhost });
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.permissions().remove(vhost, username);
			}
		});
	}
	
}
