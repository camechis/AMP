package amp.topology.core.repo.rmq;

import java.util.Collection;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Binding;
import amp.topology.core.model.BindingRepository;
import amp.topology.core.model.ClusterDoesntExistException;

public class RmqBindingRepository extends RmqBaseRepository implements BindingRepository {
	
	public RmqBindingRepository(RmqManagementFactory managementFactory) {
		super(managementFactory);
	}

	@Override
	public Collection<Binding> get(String clusterId) {
		
		logger.debug("Getting bindings for cluster {}.", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Binding>>(){

			@Override
			public Collection<Binding> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.bindings().all();
			}
		});
	}

	@Override
	public Collection<Binding> get(String clusterId, final String vhost) {
		
		logger.debug("Getting bindings for cluster {} and vhost {}.", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Binding>>(){

			@Override
			public Collection<Binding> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.bindings().all(vhost);
			}
		});
	}

	@Override
	public Collection<Binding> get(
			String clusterId, final String vhost,
			final String source, final String destination) {
		
		logger.debug("Getting bindings for cluster {} and vhost {} on exchange {} and queue/exchange {}.", 
				new Object[]{ clusterId, vhost, source, destination });
		
		return this.execute(clusterId, new ManagementFunction<Collection<Binding>>(){

			@Override
			public Collection<Binding> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.bindings().get(vhost, source, destination);
			}
		});
	}

	@Override
	public Binding get(String clusterId, 
			final String vhost, final String source,
			final String destination, final String routingkey) {
		
		logger.debug("Getting binding for cluster {} and vhost {} on exchange {} and queue/exchange {} with routing key {}.", 
				new Object[]{ clusterId, vhost, source, destination, routingkey });
		
		return this.execute(clusterId, new ManagementFunction<Binding>(){

			@Override
			public Binding perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.bindings().get(vhost, source, destination, routingkey);
			}
		});
	}

	@Override
	public void createBinding(String clusterId, final Binding binding)
			throws ClusterDoesntExistException {
		
		logger.debug("Creating binding on cluster {} and vhost {} on exchange {} and queue/exchange {} with routing key {}.",
				new Object[]{ clusterId, binding.getVhost(), binding.getSource(), 
							  binding.getDestination(), binding.getRoutingKey() });
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.bindings().create(binding);
			}
		});
	}

	@Override
	public void removeBinding(String clusterId, final Binding binding)
			throws ClusterDoesntExistException {
		
		logger.debug("Removing binding on cluster {} and vhost {}, on exchange {} and queue/exchange {} with routing key {}",
				new Object[]{ clusterId, binding.getVhost(), binding.getSource(), 
				  binding.getDestination(), binding.getRoutingKey() });
		
		this.execute(clusterId, new ManagementAction(){
			
			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.bindings().delete(
					binding.getVhost(), binding.getSource(), binding.getDestination(), binding.getPropertiesKey());
			}
			
		});
	}

}
