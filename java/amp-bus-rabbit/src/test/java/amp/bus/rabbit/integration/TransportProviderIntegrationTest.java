package amp.bus.rabbit.integration;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;

import amp.bus.EnvelopeHelper;
import amp.bus.IEnvelopeDispatcher;
import amp.bus.IEnvelopeReceivedCallback;
import amp.bus.rabbit.BasicChannelFactory;
import amp.bus.rabbit.RabbitTransportProvider;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.SimpleTopologyService;

public class TransportProviderIntegrationTest {

	private static final Logger logger = 
		LoggerFactory.getLogger(TransportProviderIntegrationTest.class);
	
	RabbitTransportProvider transport;
	
	@Before
	public void setUp() throws Exception {
		
		Broker broker = Broker.builder()
				.host("devexample.com")
				.port(5672)
				.build();
		
		SimpleTopologyService sts = 
			new SimpleTopologyService("integration-test-client", broker);
		
		BasicChannelFactory channelFactory = 
			new BasicChannelFactory("devexample", "devexample");
		
		transport = new RabbitTransportProvider(sts, channelFactory);
	}
	
	@After
	public void tearDown() throws Exception {
		
		transport.dispose();
	}

	@Test
	public void test_sending_of_a_message() throws Exception {
		
		final String messageTopic = "test.message";
		final String expectedPayload = "This is my test message.";
		
		transport.onEnvelopeReceived(new IEnvelopeReceivedCallback() {
			
			@Override
			public void handleReceive(IEnvelopeDispatcher dispatcher) {
				
				logger.debug("Message received; dispatching.");
				
				dispatcher.dispatch();
			}
		});
		
		IRegistration registration = new IRegistration(){

			@Override
			public IEnvelopeFilterPredicate getFilterPredicate() { return null; }

			@Override
			public Map<String, String> getRegistrationInfo() {
				
				HashMap<String, String> info = new HashMap<String, String>();
				
				info.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, messageTopic);
				
				return info;
			}

			@Override
			public Object handle(Envelope envelope) throws Exception {
				
				String actualPayload = new String(envelope.getPayload());
				
				assertEquals(expectedPayload, actualPayload);
				
				logger.debug("Message received, and payload asserted.");
				
				return null;
			}

			@Override
			public Object handleFailed(Envelope envelope, Exception exception) throws Exception {
				
				return null;
			}
		};
		
		transport.register(registration);
		
		Thread.sleep(1000);
		
		EnvelopeHelper helper = new EnvelopeHelper();
		helper.setMessageTopic("test.message");
		helper.setPayload(expectedPayload.getBytes());
		
		transport.send(helper.getEnvelope());
		
		Thread.sleep(1000);
	}

}
