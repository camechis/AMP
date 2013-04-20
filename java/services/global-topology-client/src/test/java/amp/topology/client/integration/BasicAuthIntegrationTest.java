package amp.topology.client.integration;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.EnvelopeHeaderConstants;

import com.berico.test.RequireProperties;
import com.berico.test.TestProperties;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.eventing.GsonSerializer;
import amp.topology.client.BasicAuthHttpClientProvider;
import amp.topology.client.HttpClientProvider;
import amp.topology.client.HttpRoutingInfoRetriever;
import amp.topology.client.IRoutingInfoRetriever;

public class BasicAuthIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(BasicAuthIntegrationTest.class);
	
	 @Rule
	 public TestProperties testProperties = new TestProperties();
	
	@Test
	@RequireProperties({
		"amp.gtc.test.hostname",
		"amp.gtc.test.basic.username",
		"amp.gtc.test.basic.password",
		"amp.gtc.test.port",
		"amp.gtc.test.url",
		"amp.gtc.test.event"
	})
	public void hit_server_with_basic_auth() throws Exception {
		
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
		
		HashMap<String, String> routingHints = new HashMap<String, String>();
		
		routingHints.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventType);
		
		RoutingInfo routingInfo = routingInfoRetriever.retrieveRoutingInfo(routingHints);
		
		logger.info("RoutingInfo: {}", routingInfo);
		
		assertTrue(true);
	}
}
