package amp.topology.resources.rmq;

import java.util.Collection;

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

import rabbitmq.mgmt.model.Binding;

import com.yammer.metrics.annotation.Timed;

import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.rmq.repo.BindingRepository;

@Path("/rmq/clusters/{cluster}")
@Produces(MediaType.APPLICATION_JSON)
public class BindingResource {

	private static final Logger logger = LoggerFactory.getLogger(BindingResource.class);
	
	BindingRepository bindingRepository;
	
	public BindingResource(BindingRepository bindingRepository){
		
		this.bindingRepository = bindingRepository;
	}
	
	@GET
	@Path("/bindings")
    @Timed
	public Collection<Binding> getBindings(@PathParam("cluster") String cluster) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all bindings on cluster {}.", cluster);
		
		return this.bindingRepository.get(cluster);
	}
	
	@GET
	@Path("/vhost/{vhost}/bindings")
    @Timed
	public Collection<Binding> getBindings(
			@PathParam("cluster") String cluster, @PathParam("vhost") String vhost) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all bindings on cluster {} and vhost {}.", cluster, vhost);
		
		return this.bindingRepository.get(cluster, vhost);
	}
	
	@GET
	@Path("/vhost/{vhost}/bindings/source/{source}/destination/{destination}")
    @Timed
	public Collection<Binding> getBindings(
			@PathParam("cluster") String cluster, @PathParam("vhost") String vhost,
			@PathParam("source") String source, @PathParam("destination") String destination) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all bindings on cluster {} vhost {} with source {} and destination {}.", 
				new Object[]{ cluster, vhost, source, destination });
		
		return this.bindingRepository.get(cluster, vhost, source, destination);
	}
	
	@GET
	@Path("/vhost/{vhost}/bindings/source/{source}/destination/{destination}/key/{key}")
    @Timed
	public Binding getBinding(
			@PathParam("cluster") String cluster, @PathParam("vhost") String vhost,
			@PathParam("source") String source, @PathParam("destination") String destination,
			@PathParam("key") String key) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting binding on cluster {} vhost {} with source {} and destination {} and key {}.", 
				new Object[]{ cluster, vhost, source, destination, key });
		
		return this.bindingRepository.get(cluster, vhost, source, destination, key);
	}
	
	@PUT
	@Path("/bindings")
    @Timed
	public Response createBinding(
			@PathParam("cluster") String cluster, Binding binding) 
			throws ClusterDoesntExistException{
		
		logger.info("Creating binding on cluster {}: {}.", cluster, binding);
		
		this.bindingRepository.createBinding(cluster, binding);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/bindings")
	@Timed
	public Response removeBinding(
			@PathParam("cluster") String cluster, Binding binding) 
			throws ClusterDoesntExistException{
		
		logger.info("Deleting binding on cluster {}: {}.", cluster, binding);
		
		this.bindingRepository.removeBinding(cluster, binding);
		
		return Response.ok().build();
	}
}
