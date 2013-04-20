package amp.topology.resources;

import java.util.Arrays;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;



import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.ITopologyService;
import amp.bus.rabbit.topology.ProducingRoute;
import amp.bus.rabbit.topology.Queue;
import amp.bus.rabbit.topology.RoutingInfo;

import cmf.bus.EnvelopeHeaderConstants;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

@Path("/topology")
@Produces(MediaType.APPLICATION_JSON)
public class TopologyServiceResource {

	private static final Logger logger = LoggerFactory.getLogger(TopologyServiceResource.class);
	
	@POST
	@Timed
	@Consumes("application/x-www-form-urlencoded")
	public RoutingInfo post(@Auth UserDetails client, MultivaluedMap<String, String> formParams) {
	    
		String topic = formParams.getFirst(EnvelopeHeaderConstants.MESSAGE_TOPIC);
		
		Exchange exchange = Exchange.builder().name("gts.test.exchange").build();
		Queue queue = Queue.builder().name("gts.test.queue").build();
		Broker broker = Broker.builder().host("localhost").build();
		
		ProducingRoute proute = ProducingRoute.builder().exchange(exchange).brokers(broker).routingKeys(topic).build();
		ConsumingRoute croute = ConsumingRoute.builder().exchange(exchange).brokers(broker).queue(queue).routingkeys(topic).build();
		
		return new RoutingInfo(Arrays.asList(proute), Arrays.asList(croute));
	}
}
