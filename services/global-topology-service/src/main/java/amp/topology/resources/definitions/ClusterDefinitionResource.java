package amp.topology.resources.definitions;

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
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.definitions.ClusterDefinition;

import com.yammer.metrics.annotation.Timed;

@Path("/definitions/cluster")
@Produces(MediaType.APPLICATION_JSON)
public class ClusterDefinitionResource {

	private static final Logger logger = 
			LoggerFactory.getLogger(ClusterDefinitionResource.class);
	
	DefinitionRepository<ClusterDefinition> repository;
	
	public ClusterDefinitionResource(
			DefinitionRepository<ClusterDefinition> repository){
		
		this.repository = repository;
	}
	
	@GET
    @Timed
	public Collection<ClusterDefinition> getAll() {
		
		logger.info("Getting all definitions.");
		
		return this.repository.all();
	}
	
	@GET
	@Path("/{id}")
    @Timed
	public ClusterDefinition get(@PathParam("id") String id) {
		
		logger.info("Getting definition with id {}.", id);
		
		return this.repository.get(id);
	}
	
	@PUT
	@Path("/{id}")
    @Timed
	public Response create(@PathParam("id") String id, ClusterDefinition definition) {
		
		logger.info("Creating definition with id {}.", id);
		
		if (definition.getId() == null){
		
			definition.setId(id);
		}
		
		boolean success = this.repository.create(definition);
		
		return (success)? Response.ok().build() 
			: Response.status(Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/{id}")
    @Timed
	public Response update(@PathParam("id") String id, ClusterDefinition definition) {
		
		logger.info("Updating definition with id {}.", id);
		
		if (definition.getId() == null){
			
			definition.setId(id);
		}
		
		boolean success = this.repository.update(definition);
		
		return (success)? Response.ok().build() 
			: Response.status(Status.BAD_REQUEST).build();
	}
	
	@DELETE
	@Path("/{id}")
    @Timed
	public Response delete(@PathParam("id") String id) {
		
		logger.info("Deleting definition with id {}.", id);
		
		boolean success = this.repository.delete(id);
		
		return (success)? Response.ok().build() 
			: Response.status(Status.BAD_REQUEST).build();
	}
}
