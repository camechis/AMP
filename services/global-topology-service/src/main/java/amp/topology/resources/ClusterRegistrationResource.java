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

import amp.topology.core.model.ClusterRegistration;
import amp.topology.core.repo.ClusterRegistrationRepository;

import com.yammer.metrics.annotation.Timed;

@Path("/clusters")
@Produces(MediaType.APPLICATION_JSON)
public class ClusterRegistrationResource {

	private static final Logger logger = LoggerFactory.getLogger(ClusterRegistrationResource.class);
	
	ClusterRegistrationRepository registrationRepository;
	
	public ClusterRegistrationResource(ClusterRegistrationRepository registrationRepository){
		
		this.registrationRepository = registrationRepository;
	}
	
	@GET
    @Timed
	public Collection<ClusterRegistration> getClusters(){
		
		logger.info("Getting all clusters.");
		
		return this.registrationRepository.all();
	}
	
	@GET
	@Path("/{id}")
    @Timed
	public ClusterRegistration getCluster(@PathParam("id") String id){
		
		logger.info("Getting cluster by Id: {}", id);
		
		return this.registrationRepository.get(id);
	}
	
	@PUT
	@Path("/{id}")
    @Timed
	public Response createCluster(@PathParam("id") String id, ClusterRegistration cluster){
		
		logger.info("Creating cluster by Id: {}", id);
		
		this.registrationRepository.create(cluster);
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/{id}")
    @Timed
	public Response updateCluster(@PathParam("id") String id, ClusterRegistration cluster){
		
		logger.info("Updating cluster by Id: {}", id);
		
		this.registrationRepository.update(cluster);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
    @Timed
	public Response deleteCluster(@PathParam("id") String id){
		
		logger.info("Deleting cluster by Id: {}", id);
		
		this.registrationRepository.delete(id);
		
		return Response.ok().build();
	}
}
