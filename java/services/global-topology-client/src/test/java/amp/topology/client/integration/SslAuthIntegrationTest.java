package amp.topology.client.integration;

import static org.junit.Assert.*;

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
import amp.topology.client.HttpClientProvider;
import amp.topology.client.HttpRoutingInfoRetriever;
import amp.topology.client.IRoutingInfoRetriever;
import amp.topology.client.SslHttpClientProvider;

public class SslAuthIntegrationTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SslAuthIntegrationTest.class);
	
	@Rule
	public TestProperties testProperties = new TestProperties();
	
	@Test
	@RequireProperties({
		"amp.gtc.test.ssl.keystore",
		"amp.gtc.test.ssl.password",
		"amp.gtc.test.port",
		"amp.gtc.test.url",
		"amp.gtc.test.event"
	})
	public void hit_server_with_ssl_auth() throws Exception {
		
		String keystoreLocation = System.getProperty("amp.gtc.test.ssl.keystore");
		String keystorePassword = System.getProperty("amp.gtc.test.ssl.password");
		int port = Integer.parseInt(System.getProperty("amp.gtc.test.port"));
		String serviceUrlExpression = System.getProperty("amp.gtc.test.url");
		String eventType = System.getProperty("amp.gtc.test.event");
		
		logger.debug("Calling GTS with SSL Auth.");
		
		HttpClientProvider provider = 
			new SslHttpClientProvider(keystoreLocation, keystorePassword, port);
		
		GsonSerializer serializer = new GsonSerializer();
		
		IRoutingInfoRetriever routingInfoRetriever = 
			new HttpRoutingInfoRetriever(provider, serviceUrlExpression, serializer);
		
		HashMap<String, String> routingHints = new HashMap<String, String>();
		
		routingHints.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventType);
		
		RoutingInfo routingInfo = routingInfoRetriever.retrieveRoutingInfo(routingHints);
		
		logger.info("Received the following RoutingInfo: {}", routingInfo);
		
		assertTrue(true);
	}

}
