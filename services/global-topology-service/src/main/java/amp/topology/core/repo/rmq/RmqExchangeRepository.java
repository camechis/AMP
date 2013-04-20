package amp.topology.core.repo.rmq;

import java.util.Collection;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Exchange;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.ExchangeRepository;

public class RmqExchangeRepository extends RmqBaseRepository implements ExchangeRepository {
	

	public RmqExchangeRepository(RmqManagementFactory managementFactory) {
		super(managementFactory);
	}

	@Override
	public void create(String clusterId, final Exchange exchange) throws ClusterDoesntExistException {
		
		logger.debug("Creating exchange [{}] on cluster {}.", exchange.getName(), clusterId);
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.exchanges().create(exchange);
			}
		});
	}

	@Override
	public void delete(String clusterId, final String vhost, final String exchangeName) throws ClusterDoesntExistException {
		
		logger.debug("Deleting exchange on cluster {}, vhost {}, with name {}", new Object[]{ clusterId, vhost, exchangeName });
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.exchanges().delete(vhost, exchangeName);
			}
		});
	}

	@Override
	public Exchange get(String clusterId, final String vhost, final String exchangeName) throws ClusterDoesntExistException {
		
		logger.debug("Getting exchange {}, on cluster {}, and vhost {}.", new Object[]{ clusterId, vhost, exchangeName });
		
		return this.execute(clusterId, new ManagementFunction<Exchange>(){

			@Override
			public Exchange perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.exchanges().get(vhost, exchangeName);
			}
		});
	}

	@Override
	public Collection<Exchange> all(String clusterId) throws ClusterDoesntExistException {
		
		logger.debug("Getting all exchanges on cluster {}.", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Exchange>>(){

			@Override
			public Collection<Exchange> perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.exchanges().all();
			}
			
		});
	}

	@Override
	public Collection<Exchange> all(String clusterId, final String vhost)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting all exchanges on cluster {} and vhost {}.", clusterId, vhost);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Exchange>>(){

			@Override
			public Collection<Exchange> perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.exchanges().allOnVHost(vhost);
			}
			
		});
	}

}
