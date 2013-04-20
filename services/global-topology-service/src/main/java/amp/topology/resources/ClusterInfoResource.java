package amp.topology.resources;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.model.ExchangeType;
import rabbitmq.mgmt.model.ListenerContext;
import rabbitmq.mgmt.model.Node;
import rabbitmq.mgmt.model.Overview;

import com.yammer.metrics.annotation.Timed;

import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.ClusterInfoRepository;

@Path("/cluster-info/{cluster}")
@Produces(MediaType.APPLICATION_JSON)
public class ClusterInfoResource {

	private static final Logger logger = LoggerFactory.getLogger(ClusterInfoResource.class);
	
	ClusterInfoRepository clusterInfoRepository;
	
	public ClusterInfoResource(ClusterInfoRepository clusterInfoRepository){
		
		this.clusterInfoRepository = clusterInfoRepository;
	}
	
	@GET
    @Timed
	public Overview getOverview(@PathParam("cluster") String cluster) throws ClusterDoesntExistException{
		
		logger.info("Getting overview for cluster {}.", cluster);
		
		return this.clusterInfoRepository.getOverview(cluster);
	}
	
	@GET
	@Path("/exchange-types")
    @Timed
	public Collection<ExchangeType> getExchangeTypes(@PathParam("cluster") String cluster) throws ClusterDoesntExistException{
		
		logger.info("Getting exchange types for cluster {}.", cluster);
		
		return this.clusterInfoRepository.getExchangeTypes(cluster);
	}
	
	@GET
	@Path("/nodes")
    @Timed
	public Collection<Node> getNodes(@PathParam("cluster") String cluster) throws ClusterDoesntExistException{
		
		logger.info("Getting nodes for cluster {}.", cluster);
		
		return this.clusterInfoRepository.getNodes(cluster);
	}
	
	@GET
	@Path("/nodes/{name}")
    @Timed
	public Node getNode(
			@PathParam("cluster") String cluster, @PathParam("name") String name) 
				throws ClusterDoesntExistException{
		
		logger.info("Getting info for node {} on cluster {}.", name, cluster);
		
		return this.clusterInfoRepository.getNode(cluster, name);
	}
	
	@GET
	@Path("/listeners")
    @Timed
	public Collection<ListenerContext> getListeners(@PathParam("cluster") String cluster) throws ClusterDoesntExistException{
		
		logger.info("Getting listeners for cluster {}.", cluster);
		
		return this.clusterInfoRepository.getListenerEndpoints(cluster);
	}
}
