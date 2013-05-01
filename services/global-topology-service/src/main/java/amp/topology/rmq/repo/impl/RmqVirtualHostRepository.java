package amp.topology.rmq.repo.impl;

import java.util.Collection;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Permission;
import rabbitmq.mgmt.model.Status;
import rabbitmq.mgmt.model.VirtualHost;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.rmq.repo.VirtualHostRepository;

public class RmqVirtualHostRepository extends RmqBaseRepository implements VirtualHostRepository {

	public RmqVirtualHostRepository(RmqManagementFactory managementFactory) {
		super(managementFactory);
	}
	
	@Override
	public void create(String clusterId, final VirtualHost vhost)
			throws ClusterDoesntExistException {
		
		this.execute(clusterId, new ManagementAction() {
			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.vhosts().create(vhost);
			}
		});
	}

	@Override
	public void delete(String clusterId, final String vhost)
			throws ClusterDoesntExistException {
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.vhosts().delete(vhost);
			}
		});
	}
	
	@Override
	public Collection<VirtualHost> getVirtualHosts(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting all virtual hosts on cluster {}.", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<VirtualHost>>(){

			@Override
			public Collection<VirtualHost> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.vhosts().all();
			}
		});
	}

	@Override
	public Status getStatus(String clusterId, final String vhost)
			throws ClusterDoesntExistException {
	
		logger.debug("Getting status of vhost {} on cluster {}.", vhost, clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Status>(){

			@Override
			public Status perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.vhosts().status(vhost);
			}
		});
	}

	@Override
	public Collection<Permission> getPermissions(String clusterId, final String vhost)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting all permissions for vhost {} on cluster {}.", vhost, clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Permission>>(){

			@Override
			public Collection<Permission> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.vhosts().permissions(vhost);
			}
		});
	}

	@Override
	public void setPermission(String clusterId, final Permission permission)
			throws ClusterDoesntExistException {
		
		logger.debug("Setting permission for user {} for vhost {} on cluster {}.", 
				new Object[]{ permission.getUser(), permission.getVhost(), clusterId });
		
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
		
		logger.debug("Removing permission for user {} for vhost {} on cluster {}.", 
				new Object[]{ username, vhost, clusterId });
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.permissions().remove(vhost, username);
			}
		});
	}

	@Override
	public VirtualHost get(String clusterId, final String virtualHostName)
			throws ClusterDoesntExistException {
		
		return this.execute(clusterId, new ManagementFunction<VirtualHost>(){
			@Override
			public VirtualHost perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.vhosts().get(virtualHostName);
			}
		});
	}
}