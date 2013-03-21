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

import amp.topology.core.ExtendedExchange;
import amp.topology.core.ITopologyRepository;

import com.yammer.metrics.annotation.Timed;

@Path("/em/exchange")
@Produces(MediaType.APPLICATION_JSON)
public class EmExchangeResource {

	private static final Logger logger = LoggerFactory.getLogger(EmExchangeResource.class);
	
	private ITopologyRepository topologyRepository;
	
	public EmExchangeResource(ITopologyRepository topologyRepository) {
		
		this.topologyRepository = topologyRepository;
		
		logger.info("Instantiated with repo {}.", topologyRepository.getClass().getCanonicalName());
	}

	@GET
	@Path("/{id}")
    @Timed
	public ExchangeWrapper getExchangeById(@PathParam("id") String id){
		
		logger.info("Getting exchange by Id: {}", id);
		
		return new ExchangeWrapper(topologyRepository.getExchange(id));
	}
	
	@GET
    @Timed
	public ExchangeListWrapper getExchanges(){
		
		logger.info("Getting exchanges.");
		
		return new ExchangeListWrapper(topologyRepository.getExchanges());
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public String createExchange(ExtendedExchange exchange){
		
		logger.info("Creating exchange: {}", exchange);
		
		topologyRepository.createExchange(exchange);
		
		return String.format("{ 'id': '%s' }", exchange.getId());
	}
	
	@POST
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Response updateExchange(ExtendedExchange exchange){
		
		logger.info("Updating exchange: {}", exchange);
		
		topologyRepository.updateExchange(exchange);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
    @Timed
	public Response removeExchange(@PathParam("id") String id){
		
		logger.info("Deleting exchange by Id: {}", id);
		
		topologyRepository.removeExchange(id);
		
		return Response.ok().build();
	}
}
