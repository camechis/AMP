package amp.bus.rabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.mockito.InOrder;

import amp.bus.rabbit.BaseChannelFactory.ConnectionContext;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class BaseChannelFactoryTest {

	@Test
	public void ensureConnectionClosed_closes_open_connections_and_swallows_exceptions() throws IOException{
		
		Connection connection = mock(Connection.class);
		
		BaseChannelFactory channelFactory = createChannelFactory(connection);
		
		ConnectionContext context = mock(ConnectionContext.class);
		
		when(context.getConnection()).thenReturn(connection);
		
		channelFactory.ensureConnectionClosed(context);
		
		verify(connection).close();
	}
	
	@Test
	public void removeConnection__connection_is_removed_from_pool_and_closed() throws IOException{
		
		Connection connection = mock(Connection.class);
		
		BaseChannelFactory channelFactory = spy(createChannelFactory(connection));
		
		ConnectionContext context = mock(ConnectionContext.class);
		
		when(context.getConnection()).thenReturn(connection);
		
		channelFactory.pooledConnections = spy(channelFactory.pooledConnections);
		
		channelFactory.pooledConnections.add(context);
		
		channelFactory.removeConnection(context);
		
		verify(channelFactory.pooledConnections).remove(context);
		
		verify(channelFactory).ensureConnectionClosed(context);
		
		verify(connection).close();
	}
	

	@Test
	public void removeConnection__connection_returns_false_if_context_doesnt_exist() throws IOException{
		
		Connection connection = mock(Connection.class);
		
		BaseChannelFactory channelFactory = spy(createChannelFactory(connection));
		
		ConnectionContext context = mock(ConnectionContext.class);
		
		when(context.getConnection()).thenReturn(connection);
		
		channelFactory.pooledConnections = spy(channelFactory.pooledConnections);
		
		boolean removed = channelFactory.removeConnection(context);
		
		verify(channelFactory.pooledConnections).remove(context);
		
		verify(channelFactory, never()).ensureConnectionClosed(context);
		
		assertFalse(removed);
	}
	
	@Test
	public void producing_route_is_returned_from_pool_if_it_exists(){
		
		Broker broker = mock(Broker.class);
		
		ProducingRoute route = mock(ProducingRoute.class);
		
		when(route.getBrokers()).thenReturn(Arrays.asList(broker));
		
		Connection connection = mock(Connection.class);
		
		ConnectionContext context = new ConnectionContext(broker, route, connection);
		
		BaseChannelFactory channelFactory = createChannelFactory(connection);
		
		channelFactory.producerReuseStrategy = spy(channelFactory.producerReuseStrategy);
		
		channelFactory.pooledConnections.add(context);
		
		ConnectionContext actualPresentContext = channelFactory.getApplicablePooledConnection(route);
		
		verify(channelFactory.producerReuseStrategy).shouldReuse(route, context);
		
		assertEquals(context, actualPresentContext);
		
		Broker notInPoolBroker = mock(Broker.class);
		
		ProducingRoute notInPoolRoute = mock(ProducingRoute.class);
		
		when(notInPoolRoute.getBrokers()).thenReturn(Arrays.asList(notInPoolBroker));
		
		ConnectionContext shouldBeNullContext = channelFactory.getApplicablePooledConnection(notInPoolRoute);
		
		assertNull(shouldBeNullContext);
	}
	
	@Test
	public void consuming_route_is_returned_from_pool_if_it_exists(){
		
		Broker broker = mock(Broker.class);
		
		ConsumingRoute route = mock(ConsumingRoute.class);
		
		when(route.getBrokers()).thenReturn(Arrays.asList(broker));
		
		Connection connection = mock(Connection.class);
		
		ConnectionContext context = new ConnectionContext(broker, route, connection);
		
		BaseChannelFactory channelFactory = createChannelFactory(connection);
		
		channelFactory.consumerReuseStrategy = spy(channelFactory.consumerReuseStrategy);
		
		channelFactory.pooledConnections.add(context);
		
		ConnectionContext actualPresentContext = channelFactory.getApplicablePooledConnection(route);
		
		verify(channelFactory.consumerReuseStrategy).shouldReuse(route, context);
		
		assertEquals(context, actualPresentContext);
		
		Broker notInPoolBroker = mock(Broker.class);
		
		ConsumingRoute notInPoolRoute = mock(ConsumingRoute.class);
		
		when(notInPoolRoute.getBrokers()).thenReturn(Arrays.asList(notInPoolBroker));
		
		ConnectionContext shouldBeNullContext = channelFactory.getApplicablePooledConnection(notInPoolRoute);
		
		assertNull(shouldBeNullContext);
	}
	
	@Test
	public void requesting_a_new_channel_from_producing_route() throws Exception {
		
		ProducingRoute route = mock(ProducingRoute.class);
		
		Collection<Broker> brokers = Arrays.asList(mock(Broker.class));
		
		when(route.getBrokers()).thenReturn(brokers);
		
		final Connection connection = mock(Connection.class);
		
		Channel channel = mock(Channel.class);
		
		when(connection.createChannel()).thenReturn(channel);
		
		BaseChannelFactory channelFactory = createChannelFactory(connection);
		
		channelFactory = spy(channelFactory);
		
		Channel actualChannel = channelFactory.getChannelFor(route);
		
		InOrder inOrder = inOrder(channelFactory);
		
		inOrder.verify(channelFactory).getApplicablePooledConnection(route);
		
		inOrder.verify(channelFactory).createConnectionContext(route);
		
		inOrder.verify(channelFactory).createChannel(any(ConnectionContext.class));
		
		assertEquals(channel, actualChannel);
	}
	
	static BaseChannelFactory createChannelFactory(final Connection connection){
		
		return new BaseChannelFactory(){

			@Override
			public ConnectionContext getConnection(Broker broker,
					ProducingRoute route) throws Exception {
				
				return new ConnectionContext(broker, route, connection);
			}

			@Override
			public ConnectionContext getConnection(Broker broker,
					ConsumingRoute route) throws Exception {
				
				return new ConnectionContext(broker, route, connection);
			}
		};
	}
	
	
}
