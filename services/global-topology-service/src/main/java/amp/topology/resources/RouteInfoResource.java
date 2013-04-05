package amp.topology.resources;

import java.util.Collection;

import javax.ws.rs.Consumes;
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

import amp.topology.core.ExtendedRouteInfo;
import amp.topology.core.ITopologyRepository;

import com.yammer.metrics.annotation.Timed;


@Path("/route")
@Produces(MediaType.APPLICATION_JSON)
public class RouteInfoResource {

	private static final Logger logger = LoggerFactory.getLogger(RouteInfoResource.class);
	
	private ITopologyRepository topologyRepository;
	
	public RouteInfoResource(ITopologyRepository topologyRepository) {
		
		this.topologyRepository = topologyRepository;
		
		logger.info("Instantiated with repo {}.", topologyRepository.getClass().getCanonicalName());
	}
	
	@GET
	@Path("/{id}")
    @Timed
	public ExtendedRouteInfo getRouteById(@PathParam("id") String id){
		
		logger.info("Getting route by Id: {}", id);
		
		return topologyRepository.getRoute(id);
	}
	
	@GET
	@Path("/client/{client}")
    @Timed
	public Collection<ExtendedRouteInfo> getRoutesByClient(@PathParam("client") String client){
		
		logger.info("Getting routes by client: {}", client);
		
		return topologyRepository.getRoutesByClient(client);
	}
	
	@GET
	@Path("/topic/{topic}")
    @Timed
	public Collection<ExtendedRouteInfo> getRoutesByTopic(@PathParam("topic") String topic){
		
		logger.info("Getting routes by topic: {}", topic);
		
		return topologyRepository.getRoutesByTopic(topic);
	}
	
	@GET
    @Timed
	public Collection<ExtendedRouteInfo> getRoutes(){
		
		logger.info("Getting routes");
		
		return topologyRepository.getRoutes();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public String createRoute(ExtendedRouteInfo route){
		
		logger.info("Creating route: {}", route);
		
		topologyRepository.createRoute(route);
		
		// JSON.parse will barf if you don't use double quotes
		return String.format("{ \"id\": \"%s\" }", route.getId());
	}
	
	@POST
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Response updateRoute(ExtendedRouteInfo route){
		
		logger.info("Updating route: {}", route);
		
		topologyRepository.updateRoute(route);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
    @Timed
	public Response removeRoute(@PathParam("id") String id){
		
		logger.info("Deleting route by Id: {}", id);
		
		topologyRepository.removeRoute(id);
		
		return Response.ok().build();
	}
}
