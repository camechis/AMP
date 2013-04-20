package amp.topology.resources;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.model.Queue;

import com.yammer.metrics.annotation.Timed;

import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.QueueRepository;

@Path("/clusters/{cluster}/vhost/{vhost}/queues")
@Produces(MediaType.APPLICATION_JSON)
public class QueueResource {

	private static final Logger logger = LoggerFactory.getLogger(QueueResource.class);
	
	QueueRepository queueRepository;
	
	public QueueResource(QueueRepository queueRepository){
		
		this.queueRepository = queueRepository;
	}
	
	@GET
    @Timed
	public Collection<Queue> getQueues(
		@PathParam("cluster") String cluster, @PathParam("vhost") String vhost) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all queues on {} at vhost {}.", cluster, vhost);
		
		if (vhost.equals("*")){
			
			return this.queueRepository.all(cluster);
		} 
		
		return this.queueRepository.all(cluster, vhost);
	}
	
	
	@GET
	@Path("/{name}")
    @Timed
	public Queue getQueue(
		@PathParam("cluster") String cluster, @PathParam("vhost") String vhost, @PathParam("name") String name) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting queue on {} vhost {} with name {}.", new Object[]{ cluster, vhost, name });
		
		return this.queueRepository.get(cluster, vhost, name);
	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Response createQueue(
		@PathParam("cluster") String cluster, Queue queue)
			throws ClusterDoesntExistException{
		
		logger.info("Creating queue with name {} on cluster {} vhost {}.", 
			new Object[]{ queue.getName(), queue.getVhost(), cluster });
		
		this.queueRepository.create(cluster, queue);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{name}")
    @Timed
	public Response deleteQueue(
		@PathParam("cluster") String cluster, @PathParam("vhost") String vhost, @PathParam("name") String name) 
			throws ClusterDoesntExistException{
		
		logger.info("Deleting queue with name {} on cluster {} vhost {}.", 
			new Object[]{ name, cluster, vhost });
		
		this.queueRepository.delete(cluster, vhost, name);
		
		return Response.ok().build();
	}
	
}
