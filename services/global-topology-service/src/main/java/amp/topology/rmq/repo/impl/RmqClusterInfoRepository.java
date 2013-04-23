package amp.topology.rmq.repo.impl;

import java.util.Collection;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.ExchangeType;
import rabbitmq.mgmt.model.ListenerContext;
import rabbitmq.mgmt.model.Node;
import rabbitmq.mgmt.model.Overview;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.rmq.repo.ClusterInfoRepository;

public class RmqClusterInfoRepository extends RmqBaseRepository implements ClusterInfoRepository {

	public RmqClusterInfoRepository(RmqManagementFactory managementFactory) {
		super(managementFactory);
	}

	@Override
	public Overview getOverview(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting overview for cluster {}", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Overview>(){

			@Override
			public Overview perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.overview();
			}});
	}

	@Override
	public Collection<ExchangeType> getExchangeTypes(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting exchange types for cluster {}", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<ExchangeType>>(){

			@Override
			public Collection<ExchangeType> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.overview().getExchangeTypes();
			}});
	}

	@Override
	public Collection<Node> getNodes(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting nodes for cluster {}", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<Node>>(){

			@Override
			public Collection<Node> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.nodes().all();
			}});
	}

	@Override
	public Node getNode(String clusterId, final String name)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting node {} on cluster {}", name, clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Node>(){

			@Override
			public Node perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.nodes().get(name);
			}});
	}

	@Override
	public Collection<ListenerContext> getListenerEndpoints(String clusterId)
			throws ClusterDoesntExistException {
		
		logger.debug("Getting listener endpoints for cluster {}", clusterId);
		
		return this.execute(clusterId, new ManagementFunction<Collection<ListenerContext>>(){

			@Override
			public Collection<ListenerContext> perform(RabbitMgmtService mgmtService)
					throws Exception {
				
				return mgmtService.overview().getListeners();
			}});
	}

}
