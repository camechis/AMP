package amp.topology.resources;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.model.Cluster;
import amp.topology.core.repo.ClusterRepository;

import com.yammer.metrics.annotation.Timed;

@Path("/clusters")
@Produces(MediaType.APPLICATION_JSON)
public class ClusterResource {

	private static final Logger logger = LoggerFactory.getLogger(ClusterResource.class);
	
	ClusterRepository clusterRepository;
	
	public ClusterResource(ClusterRepository clusterRepository){
		
		this.clusterRepository = clusterRepository;
	}
	
	@GET
    @Timed
	public Collection<Cluster> getClusters(){
		
		logger.info("Getting all clusters.");
		
		return this.clusterRepository.all();
	}
	
	@GET
	@Path("/{id}")
    @Timed
	public Cluster getCluster(@PathParam("id") String id){
		
		logger.info("Getting cluster by Id: {}", id);
		
		return this.clusterRepository.get(id);
	}
	
	@PUT
	@Path("/{id}")
    @Timed
	public Response createCluster(@PathParam("id") String id, Cluster cluster){
		
		logger.info("Creating cluster by Id: {}", id);
		
		this.clusterRepository.create(cluster);
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/{id}")
    @Timed
	public Response updateCluster(@PathParam("id") String id, Cluster cluster){
		
		logger.info("Updating cluster by Id: {}", id);
		
		this.clusterRepository.update(cluster);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
    @Timed
	public Response deleteCluster(@PathParam("id") String id){
		
		logger.info("Deleting cluster by Id: {}", id);
		
		this.clusterRepository.delete(id);
		
		return Response.ok().build();
	}
}
