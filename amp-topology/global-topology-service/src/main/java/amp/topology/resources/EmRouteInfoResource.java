package amp.topology.resources;

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


@Path("/em/route_infos")
@Produces(MediaType.APPLICATION_JSON)
public class EmRouteInfoResource {

	private static final Logger logger = LoggerFactory.getLogger(EmRouteInfoResource.class);
	
	private ITopologyRepository topologyRepository;
	
	public EmRouteInfoResource(ITopologyRepository topologyRepository) {
		
		this.topologyRepository = topologyRepository;
		
		logger.info("Instantiated with repo {}.", topologyRepository.getClass().getCanonicalName());
	}
	
	@GET
	@Path("/{id}")
    @Timed
	public RouteInfoWrapper getRouteById(@PathParam("id") String id){
		
		logger.info("Getting route by Id: {}", id);
		
		return new RouteInfoWrapper(topologyRepository.getRoute(id));
	}
	
	@GET
    @Timed
	public RouteInfoListWrapper getRoutes(){
		
		logger.info("Getting routes");
		
		return new RouteInfoListWrapper(topologyRepository.getRoutes());
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public String createRoute(ExtendedRouteInfo route){
		
		logger.info("Creating route: {}", route);
		
		topologyRepository.createRoute(route);
		
		return String.format("{ 'id': '%s' }", route.getId());
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
