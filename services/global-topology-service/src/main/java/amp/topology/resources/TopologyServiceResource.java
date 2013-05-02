package amp.topology.resources;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.RoutingInfoController;
import amp.topology.core.model.Client;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

@Path("/topology")
@Produces(MediaType.APPLICATION_JSON)
public class TopologyServiceResource {

	private static final Logger logger = LoggerFactory.getLogger(TopologyServiceResource.class);
	
	RoutingInfoController routingInfoController;
	
	public TopologyServiceResource(RoutingInfoController routingInfoController){
		
		this.routingInfoController = routingInfoController;
	}
	
	
	@POST
	@Timed
	@Consumes("application/x-www-form-urlencoded")
	public RoutingInfo post(@Auth UserDetails userDetails, MultivaluedMap<String, String> formParams) throws Exception {
	    
		if (formParams == null) throw new Exception("No routing context was submitted with the request.");
		
		Map<String, String> context = Utils.createRoutingContext(formParams);
		
		Client client = Utils.convertUserDetails(userDetails);
		
		logger.info("Getting routing info for client: {}, with context: {}", client.getPrincipalName(), context);
		
		RoutingInfo ri = routingInfoController.getRouteFromContext(client, context);
		
		logger.info("Controller returned with the following route: {}", ri);
		
		return ri;
		
		/*
		String topic = formParams.getFirst(EnvelopeHeaderConstants.MESSAGE_TOPIC);
		
		Exchange exchange = Exchange.builder().name("gts.test.exchange").build();
		Queue queue = Queue.builder().name("gts.test.queue").build();
		Broker broker = Broker.builder().host("localhost").build();
		
		ProducingRoute proute = ProducingRoute.builder().exchange(exchange).brokers(broker).routingKeys(topic).build();
		ConsumingRoute croute = ConsumingRoute.builder().exchange(exchange).brokers(broker).queue(queue).routingkeys(topic).build();
		
		return new RoutingInfo(Arrays.asList(proute), Arrays.asList(croute));
		*/
	}
}
