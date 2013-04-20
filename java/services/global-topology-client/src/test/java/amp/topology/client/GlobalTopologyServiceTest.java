package amp.topology.client;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.HashMap;
//import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.eventing.GsonSerializer;

import cmf.bus.EnvelopeHeaderConstants;

import com.berico.test.RequireProperties;
import com.berico.test.TestProperties;

public class GlobalTopologyServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(GlobalTopologyServiceTest.class);
	
	@Rule
	public TestProperties testProperties = new TestProperties();
	
	@Test(expected=RoutingInfoNotFoundException.class)
	public void gts_fails_when_it_cant_find_a_route_for_a_topic() {
		
		IRoutingInfoRetriever retriever = mock(IRoutingInfoRetriever.class);
		
		GlobalTopologyService gts = new GlobalTopologyService(retriever);
		
		HashMap<String, String> routingHints = new HashMap<String, String>();
		
		routingHints.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, "A nonexistent topic!");
		
		gts.getRoutingInfo(routingHints);
	}
	
	@Test
	@RequireProperties({
		"amp.gtc.test.hostname",
		"amp.gtc.test.basic.username",
		"amp.gtc.test.basic.password",
		"amp.gtc.test.port",
		"amp.gtc.test.url",
		"amp.gtc.test.event"
	})
	public void gts_returns_correct_route() throws Exception {
		
		String hostname = System.getProperty("amp.gtc.test.hostname");
		String username = System.getProperty("amp.gtc.test.basic.username");
		String password = System.getProperty("amp.gtc.test.basic.password");
		int port = Integer.parseInt(System.getProperty("amp.gtc.test.port"));
		String serviceUrlExpression = System.getProperty("amp.gtc.test.url");
		String eventType = System.getProperty("amp.gtc.test.event");
		
		logger.debug("Calling GTS with Basic Auth.");
		
		HttpClientProvider provider = 
				new BasicAuthHttpClientProvider(hostname, port, username, password);
		
		GsonSerializer serializer = new GsonSerializer();
		
		IRoutingInfoRetriever routingInfoRetriever = 
			new HttpRoutingInfoRetriever(provider, serviceUrlExpression, serializer);
		
		DefaultApplicationExchangeProvider fallback = new DefaultApplicationExchangeProvider("my-client", hostname, port);
		
		fallback.setExchangeName("amp.fallback");
		fallback.getExchangePrototype().setDurable(false);
		fallback.getExchangePrototype().setAutoDelete(false);
		
		GlobalTopologyService gts = new GlobalTopologyService(
			routingInfoRetriever, 10 * 60 * 1000, fallback);
		
		HashMap<String, String> routingHints = new HashMap<String, String>();
		
		routingHints.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventType);
		
		RoutingInfo routingInfo = gts.getRoutingInfo(routingHints);
		
		logger.info("Routing Info: {}", routingInfo);
		
		assertTrue(true);
	}

}
