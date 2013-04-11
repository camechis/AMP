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

import rabbitmq.mgmt.model.Exchange;

import com.yammer.metrics.annotation.Timed;

import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.model.ExchangeRepository;

@Path("/clusters/{cluster}/vhost/{vhost}/exchanges")
@Produces(MediaType.APPLICATION_JSON)
public class NExchangeResource {

	private static final Logger logger = LoggerFactory.getLogger(NExchangeResource.class);
	
	ExchangeRepository exchangeRepository;
	
	public NExchangeResource(ExchangeRepository exchangeRepository){
		
		this.exchangeRepository = exchangeRepository;
	}
	
	@GET
    @Timed
	public Collection<Exchange> getExchanges(
		@PathParam("cluster") String cluster, @PathParam("vhost") String vhost) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all exchanges on {} at vhost {}.", cluster, vhost);
		
		if (vhost.equals("*")){
			
			return this.exchangeRepository.all(cluster);
		} 
		
		return this.exchangeRepository.all(cluster, vhost);
	}
	
	
	@GET
	@Path("/{name}")
    @Timed
	public Exchange getExchange(
		@PathParam("cluster") String cluster, @PathParam("vhost") String vhost, @PathParam("name") String name) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting exchange on {} vhost {} with name {}.", new Object[]{ cluster, vhost, name });
		
		return this.exchangeRepository.get(cluster, vhost, name);
	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Response createExchange(
		@PathParam("cluster") String cluster, Exchange exchange)
			throws ClusterDoesntExistException{
		
		logger.info("Creating exchange with name {} on cluster {} vhost {}.", 
			new Object[]{ exchange.getName(), exchange.getVhost(), cluster });
		
		this.exchangeRepository.create(cluster, exchange);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{name}")
    @Timed
	public Response deleteExchange(
		@PathParam("cluster") String cluster, @PathParam("vhost") String vhost, @PathParam("name") String name) 
			throws ClusterDoesntExistException{
		
		logger.info("Deleting exchange with name {} on cluster {} vhost {}.", 
			new Object[]{ name, cluster, vhost });
		
		this.exchangeRepository.delete(cluster, vhost, name);
		
		return Response.ok().build();
	}
	
}
