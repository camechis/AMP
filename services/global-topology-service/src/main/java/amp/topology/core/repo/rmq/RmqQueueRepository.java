package amp.topology.core.repo.rmq;

import java.util.Collection;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Queue;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.QueueRepository;

public class RmqQueueRepository extends RmqBaseRepository implements QueueRepository {

	public RmqQueueRepository(RmqManagementFactory managementFactory) {
		super(managementFactory);
	}
	
	@Override
	public void create(String clusterId, final Queue queue)
			throws ClusterDoesntExistException {
		
		logger.debug("Creating queue with name {} on cluster {}.", queue.getName(), clusterId);
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.queues().create(queue);
			}
		});
	}

	@Override
	public void delete(String clusterId, final String vhost, final String queueName)
			throws ClusterDoesntExistException {
		
		logger.debug("Deleting queue with name {} on cluster {} and vhost {}.", new Object[]{ queueName, clusterId, vhost });
		
		this.execute(clusterId, new ManagementAction(){

			@Override
			public void perform(RabbitMgmtService mgmtService) throws Exception {
				
				mgmtService.queues().delete(vhost, queueName);
			}
		});
	}

	@Override
	public Queue get(String clusterId, final String vhost, final String queueName)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting queue with name {} on cluster {} and vhost {}.", new Object[]{ queueName, clusterId, vhost });
		
		return this.execute(clusterId, new ManagementFunction<Queue>(){

			@Override
			public Queue perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.queues().get(vhost, queueName);
			}});
	}

	@Override
	public Collection<Queue> all(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting all queues on cluster {}", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Queue>>(){

			@Override
			public Collection<Queue> perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.queues().all();
			}
		});
	}

	@Override
	public Collection<Queue> all(String clusterId, final String vhost)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting all queues on cluster {} and vhost {}.", clusterId, vhost);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Queue>>(){

			@Override
			public Collection<Queue> perform(RabbitMgmtService mgmtService) throws Exception {
				
				return mgmtService.queues().allOnVHost(vhost);
			}
		});
	}

}
