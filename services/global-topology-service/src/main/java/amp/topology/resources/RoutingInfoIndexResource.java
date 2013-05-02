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
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.factory.index.RoutingInfoIndex;
import amp.topology.core.factory.index.RoutingInfoSelectionContext;

import com.yammer.metrics.annotation.Timed;

@Path("/index")
@Produces(MediaType.APPLICATION_JSON)
public class RoutingInfoIndexResource {

	private static final Logger logger = LoggerFactory.getLogger(RoutingInfoIndexResource.class);
	
	RoutingInfoIndex routingInfoIndex;
	
	public RoutingInfoIndexResource(RoutingInfoIndex routingInfoIndex){
		
		this.routingInfoIndex = routingInfoIndex;
	}
	
	@GET
	@Path("/entries")
    @Timed
	public Collection<RoutingInfoSelectionContext> getAll() {
		
		logger.info("Getting all entries.");
		
		return this.routingInfoIndex.all();
	}
	
	@GET
	@Path("/entries/{id}")
    @Timed
	public RoutingInfoSelectionContext get(@PathParam("id") String id) {
		
		logger.info("Getting entry with id {}.", id);
		
		return this.routingInfoIndex.get(id);
	}
	
	@PUT
	@Path("/entries/{id}")
    @Timed
	public Response create(@PathParam("id") String id, RoutingInfoSelectionContext context) {
		
		logger.info("Creating context with id {}.", id);
		
		if (context.getId() == null){
		
			context.setId(id);
		}
		
		boolean success = this.routingInfoIndex.create(context);
		
		return (success)? Response.ok().build() 
			: Response.status(Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/entries/{id}")
    @Timed
	public Response update(@PathParam("id") String id, RoutingInfoSelectionContext definition) {
		
		logger.info("Updating entry with id {}.", id);
		
		if (definition.getId() == null){
			
			definition.setId(id);
		}
		
		boolean success = this.routingInfoIndex.update(definition);
		
		return (success)? Response.ok().build() 
			: Response.status(Status.BAD_REQUEST).build();
	}
	
	@DELETE
	@Path("/entries/{id}")
    @Timed
	public Response delete(@PathParam("id") String id) {
		
		logger.info("Deleting entry with id {}.", id);
		
		boolean success = this.routingInfoIndex.delete(id);
		
		return (success)? Response.ok().build() 
			: Response.status(Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/topics/{query}")
	@Timed
	public Collection<String> topics(@PathParam("query") String query){
		
		return this.routingInfoIndex.topics(query);
	}
	
	@GET
	@Path("/patterns/{query}")
	@Timed
	public Collection<String> patterns(@PathParam("query") String query){
		
		return this.routingInfoIndex.messagePatterns(query);
	}
	
	@GET
	@Path("/clients/{query}")
	@Timed
	public Collection<String> clients(@PathParam("query") String query){
		
		return this.routingInfoIndex.clients(query);
	}
	
	@GET
	@Path("/groups/{query}")
	@Timed
	public Collection<String> groups(@PathParam("query") String query){
		
		return this.routingInfoIndex.groups(query);
	}
}
