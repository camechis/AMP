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

import amp.topology.core.ExtendedExchange;
import amp.topology.core.ITopologyRepository;

import com.yammer.metrics.annotation.Timed;

@Path("/exchange")
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeResource {

	private static final Logger logger = LoggerFactory.getLogger(ExchangeResource.class);
	
	private ITopologyRepository topologyRepository;
	
	public ExchangeResource(ITopologyRepository topologyRepository) {
		
		this.topologyRepository = topologyRepository;
		
		logger.info("Instantiated with repo {}.", topologyRepository.getClass().getCanonicalName());
	}

	@GET
	@Path("/{id}")
    @Timed
	public ExtendedExchange getExchangeById(@PathParam("id") String id){
		
		logger.info("Getting exchange by Id: {}", id);
		
		return topologyRepository.getExchange(id);
	}
	
	@GET
	@Path("/broker/{host}")
	public Collection<ExtendedExchange> getExchangesByBroker(@PathParam("host") String host){
		
		logger.info("Getting exchange by broker: {}", host);
		
		return topologyRepository.getExchangesByBroker(host);
	}
	
	@GET
	@Path("/broker/{host}/port/{port}")
	public Collection<ExtendedExchange> getExchangesByBroker(
			@PathParam("host") String host, @PathParam("port") int port){
		
		logger.info("Getting exchange by broker: {}:{}", host, port);
		
		return topologyRepository.getExchangesByBroker(host, port);
	}
	
	@GET
	@Path("/broker/{host}/port/{port}/vhost/{vhost}")
	public Collection<ExtendedExchange> getExchangesByBroker(
			@PathParam("host") String host, @PathParam("port") int port, 
			@PathParam("vhost") String vhost){
		
		String virtualHost = decodeSlashes(vhost);
		
		logger.info("Getting exchange by broker: {}:{}{}", new Object[]{ host, port, virtualHost });
		
		return topologyRepository.getExchangesByBroker(host, port, virtualHost);
	}
	
	@GET
    @Timed
	public Collection<ExtendedExchange> getExchanges(){
		
		logger.info("Getting exchanges.");
		
		return topologyRepository.getExchanges();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public String createExchange(ExtendedExchange exchange){
		
		logger.info("Creating exchange: {}", exchange);
		
		topologyRepository.createExchange(exchange);
		
		// JSON.parse will barf if you don't use double quotes
		return String.format("{ \"id\": \"%s\" }", exchange.getId());
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
	
	static String decodeSlashes(String string){
		
		return string.replace("%2F", "/");
	}
}
