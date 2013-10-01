package amp.rabbit.connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import amp.messaging.MessageRegistration;
import amp.messaging.NullHandler;
import amp.messaging.NullMessageProcessor;
import amp.rabbit.topology.Exchange;
import amp.rabbit.topology.SimpleTopologyService;
import amp.rabbit.transport.RabbitTransportProvider;
import amp.rabbit.transport.SimpleRoutingInfoCache;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionManagementTests {
	
    private List<Connection> _connections;
    private List<Channel> _channels;
    private ConnectionFactory _rmqFactory;
    
    private RabbitTransportProvider _transport;

    @Before
	public void setUp() throws Exception {

        _connections = new ArrayList<Connection>();
        _channels = new ArrayList<Channel>();
        _rmqFactory = mock(ConnectionFactory.class);
        
        when(_rmqFactory.newConnection()).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				
		           Connection connection = mock(Connection.class);
		            _connections.add(connection);

		            when(connection.createChannel()).thenAnswer(new Answer() {
						@Override
						public Object answer(InvocationOnMock invocation) throws Throwable {
							
			                Channel channel = mock(Channel.class);
			                _channels.add(channel);
			                
			                return channel;
			             }
		            });

			        return connection;
	               
            }
        });

        _transport = new RabbitTransportProvider(
            new SimpleTopologyService(null, "test", "nowhere.com", "/", 0),
            new TestConnectionFactory(_rmqFactory),
            new SimpleRoutingInfoCache(100));
   	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
    public void Send_should_create_a_connection_and_a_channel() throws Exception {
		
        Envelope env = new Envelope();
        env.setHeader(EnvelopeHeaderConstants.MESSAGE_TOPIC, "testing");

        _transport.send(env);

        verify(_rmqFactory).newConnection();
        verify(_connections.get(0)).createChannel();
    }

	@Test
    public void Register_should_create_a_connection_and_a_channel() throws Exception {
		Envelope env = new Envelope();
        env.setHeader(EnvelopeHeaderConstants.MESSAGE_TOPIC, "testing");

        _transport.register(new MessageRegistration(new NullMessageProcessor(), new NullHandler(),new IEnvelopeFilterPredicate() {
			
			@Override
			public boolean filter(Envelope envelope) {
				return true;
			}
		}));

        verify(_rmqFactory, timeout(250)).newConnection();
        verify(_connections.get(0)).createChannel();
    }
	
    private static class TestConnectionFactory extends BaseConnectionFactory {
    	
        private final ConnectionFactory _factory;
        
        public TestConnectionFactory(ConnectionFactory factory) {
            _factory = factory;
        }

        @Override
        protected ConnectionManager createConnectionManager(Exchange exchange) throws IOException {
            return new ConnectionManager(_factory);    
        }
    }
}
