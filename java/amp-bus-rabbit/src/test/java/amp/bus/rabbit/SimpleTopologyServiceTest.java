package amp.bus.rabbit;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.EnvelopeHeaderConstants;

import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;
import amp.bus.rabbit.topology.RoutingInfo;
import amp.bus.rabbit.topology.SimpleTopologyService;

public class SimpleTopologyServiceTest {

	private static final Logger logger = 
		LoggerFactory.getLogger(SimpleTopologyServiceTest.class);
	
	@Test
	public void topology_service_uses_prototypes_for_default_settings() {
		
		String messageTopic = "some.message.topic";
		
		HashMap<String, String> info = new HashMap<String, String>();
		
		info.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, messageTopic);
		
		Broker broker = Broker.builder()
				.host("devexample.com")
				.port(5672)
				.build();
		
		SimpleTopologyService sts = 
			new SimpleTopologyService("integration-test-client", broker);
		
		RoutingInfo ri = sts.getRoutingInfo(info);
		
		ProducingRoute proute = ri.getProducingRoutes().get(0);
		
		assertEquals("amq.direct", proute.getExchange().getName());
		assertEquals("/", proute.getExchange().getVirtualHost());
		assertEquals("direct", proute.getExchange().getExchangeType());
		assertEquals(false, proute.getExchange().isAutoDelete());
		assertEquals(false, proute.getExchange().isDurable());
		assertEquals(false, proute.getExchange().shouldDeclare());
		
		Broker prouteActual = proute.getBrokers().iterator().next();
		
		assertEquals(broker, prouteActual);
		
		ConsumingRoute croute = ri.getConsumingRoutes().get(0);
		
		assertEquals("amq.direct", croute.getExchange().getName());
		assertEquals("/", croute.getExchange().getVirtualHost());
		assertEquals("direct", croute.getExchange().getExchangeType());
		assertEquals(false, croute.getExchange().isAutoDelete());
		assertEquals(false, croute.getExchange().isDurable());
		assertEquals(false, croute.getExchange().shouldDeclare());
		
		Broker crouteActual = croute.getBrokers().iterator().next();
		
		assertEquals(broker, crouteActual);
	}

}
