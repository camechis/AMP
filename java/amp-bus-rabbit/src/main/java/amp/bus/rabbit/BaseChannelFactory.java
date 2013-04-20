package amp.bus.rabbit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.BaseRoute;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;
import cmf.bus.IDisposable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Provides a lot of the boiler-plate functionality most Rabbit Connection Factories
 * would need to function properly. This base implementation imposes only one responsibility
 * on deriving classes: that they create and supply the connection to the broker (which
 * is where most configuration like authentication is done). 
 *  
 * @author Richard Clayton (Berico Technologies)
 *
 */
public abstract class BaseChannelFactory implements IRabbitChannelFactory, IDisposable {

	private static final Logger logger = LoggerFactory.getLogger(BaseChannelFactory.class);

	public static int HEARTBEAT_INTERVAL = 2;
	public ConnectionReuseStrategy consumerReuseStrategy = ConnectionReuseStrategy.SAME_BROKER;
	public ConnectionReuseStrategy producerReuseStrategy = ConnectionReuseStrategy.SAME_BROKER;
	
	CopyOnWriteArrayList<ConnectionContext> pooledConnections =
		  new CopyOnWriteArrayList<ConnectionContext>();
	
	/**
	 * Create a new instance of the ChannelFactory.
	 */
	public BaseChannelFactory() {}
	
	/**
	 * Convenience method to allow setting of the heartbeat interval via DI container.
	 * @param interval 
	 */
	public void setHeartbeatInterval(int interval){
		HEARTBEAT_INTERVAL = interval;
	}
	
	/**
	 * Derived classes have the sole responsibility of providing connections
	 * to this class (however that is done: username/password, certificates, etc.).
	 * @param broker This is the specific broker to attempt to make the connection with.
	 * @param route Contextual information that may be needed when making the connection.
	 * @return Context of the Connection
	 */
	public abstract ConnectionContext getConnection(Broker broker, ProducingRoute route) throws Exception;
	
	/**
	 * Derived classes have the sole responsibility of providing connections
	 * to this class (however that is done: username/password, certificates, etc.).
	 * @param broker This is the specific broker to attempt to make the connection with.
	 * @param route Contextual information that may be needed when making the connection.
	 * @return Context of the Connection
	 */
	public abstract ConnectionContext getConnection(Broker broker, ConsumingRoute route) throws Exception;
	
	/**
	 * Get an AMQP channel for the supplied route.
	 * 
	 * @param route Route to get the channel for.
	 * @return AMQP Channel
	 */
	@Override
	public Channel getChannelFor(ProducingRoute route) throws Exception {
		
		logger.trace("Getting channel for route: {}", route);
		
		ConnectionContext context = getApplicablePooledConnection(route);
		
		if (context != null){
			
			System.out.println("Context not null");
			
			if (!context.getProducingRoutes().contains(route)){
				
				context.getProducingRoutes().add(route);
			}
		}
		else {
			
			System.out.println("Context is null");
			
			context = createConnectionContext(route);
		}
		
		return createChannel(context);
	}
	
	/**
	 * Get an AMQP channel for the supplied route.
	 * 
	 * @param route Route to get the channel for.
	 * @return AMQP Channel
	 */
	@Override
	public Channel getChannelFor(ConsumingRoute route) throws Exception {
		
		logger.trace("Getting channel for route: {}", route);
		
		ConnectionContext context = getApplicablePooledConnection(route);
		
		if (context != null){
			
			if (!context.getConsumingRoutes().contains(route)){
				
				context.getConsumingRoutes().add(route);
			}
		}
		else {
			
			context = createConnectionContext(route);
		}
		
		return createChannel(context);
	}
	
	/**
	 * Create a new connection context from the supplied route.
	 * 
	 * @param route Route to get an AMQP Connection.
	 * @return Context of the connection.
	 */
	ConnectionContext createConnectionContext(BaseRoute route){
		
		for (Broker broker : route.getBrokers()){
			
			try {
			
				ConnectionContext context = null;
				
				if (ProducingRoute.class.isInstance(route)){
					
					context = getConnection(broker, (ProducingRoute)route);
				}
				else {
					
					context = getConnection(broker, (ConsumingRoute)route);
				}
				
				return context;
			}
			catch (Exception e){
				
				logger.warn("Failed to establish connection to broker: {}; ex: {}", broker, e);
			}
		}
		
		return null;
	}
	
	/**
	 * Given a connection context, create a new channel.
	 * @param context Context of the Connection.
	 * @return AMQP Channel
	 * @throws IOException
	 */
	Channel createChannel(ConnectionContext context) throws IOException {
		
		context.getConnection().addShutdownListener(new RabbitConnectionShutdownListener(this, context));
		
		Channel channel = context.getConnection().createChannel();
		
		return channel;
	}
	
	/**
	 * Get a pooled connection if the route applicable to the current
	 * producing route reuse strategy.
	 * 
	 * @param route Route to test for applicability
	 * @return An active connection context or null
	 */
	ConnectionContext getApplicablePooledConnection(ProducingRoute route){
		
		for (ConnectionContext context : pooledConnections){
			
			if (producerReuseStrategy.shouldReuse(route, context)){
				
				return context;
			}
		}
		return null;
	}
	
	/**
	 * Get a pooled connection if the route applicable to the current
	 * producing route reuse strategy.
	 * 
	 * @param route Route to test for applicability
	 * @return An active connection context or null
	 */
	ConnectionContext getApplicablePooledConnection(ConsumingRoute route){
		
		for (ConnectionContext context : pooledConnections){
			
			if (consumerReuseStrategy.shouldReuse(route, context)){
				
				return context;
			}
		}
		return null;
	}
	
	/**
	 * Remove the connection, closing it if necessary.
	 * 
	 * @param context Context of the Connection
	 * @return true if successful
	 */
	public boolean removeConnection(ConnectionContext context){
		
		boolean removed = this.pooledConnections.remove(context);
		
		if (removed && context != null){
			
			ensureConnectionClosed(context);
		}
		
		return removed;
	}
	
	/**
	 * Iterate over pooled connections, closing each connection.
	 */
	@Override
	public void dispose() {
		
		for (ConnectionContext context : this.pooledConnections){
			
			ensureConnectionClosed(context);
		}
	}
	
	/**
	 * Ensure that the connection held by the context is closed.
	 * 
	 * @param context Connection Context
	 */
	void ensureConnectionClosed(ConnectionContext context){
		
		try {
			
			context.getConnection().close();
			
		} catch (IOException e) {
			
			logger.error("Problem closing connection: {}", e);
		}
	}
	
	/**
	 * Represents a connection with a broker, and the routes using that connection.
	 * 
	 * @author Richard Clayton (Berico Technologies)
	 */
	public static class ConnectionContext {
		
		Broker activeBroker;
		ArrayList<ProducingRoute> registeredProducingRoutes = new ArrayList<ProducingRoute>();
		ArrayList<ConsumingRoute> registeredConsumingRoutes = new ArrayList<ConsumingRoute>();
		Connection connection;
		
		public ConnectionContext(Broker activeBroker,
				ProducingRoute producingRoute,
				Connection connection) {
			
			this.activeBroker = activeBroker;
			this.registeredProducingRoutes.add(producingRoute);
			this.connection = connection;
		}
		
		public ConnectionContext(Broker activeBroker,
				ConsumingRoute consumingRoute,
				Connection connection) {
			
			this.activeBroker = activeBroker;
			this.registeredConsumingRoutes.add(consumingRoute);
			this.connection = connection;
		}
		
		public ConnectionContext(Broker activeBroker,
				Collection<ProducingRoute> producingRoutes,
				Collection<ConsumingRoute> consumingRoutes, 
				Connection connection) {
			
			this.activeBroker = activeBroker;
			this.registeredProducingRoutes.addAll(producingRoutes);
			this.registeredConsumingRoutes.addAll(consumingRoutes);
			this.connection = connection;
		}

		public Broker getActiveBroker() {
			return activeBroker;
		}
		
		public ArrayList<ProducingRoute> getProducingRoutes() {
			return registeredProducingRoutes;
		}
		
		public ArrayList<ConsumingRoute> getConsumingRoutes() {
			return registeredConsumingRoutes;
		}
		
		public Connection getConnection() {
			return connection;
		}
		
		public boolean remove(BaseRoute route){
			
			if (route instanceof ProducingRoute){
				
				return registeredProducingRoutes.remove(route);
			}
			else {
				
				return registeredConsumingRoutes.remove(route);
			}
		}
		
		public boolean hasActiveRoutes(){
			
			return registeredConsumingRoutes.size() > 0 
				|| registeredProducingRoutes.size() > 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((activeBroker == null) ? 0 : activeBroker.hashCode());
			result = prime * result
					+ ((connection == null) ? 0 : connection.hashCode());
			result = prime
					* result
					+ ((registeredConsumingRoutes == null) ? 0
							: registeredConsumingRoutes.hashCode());
			result = prime
					* result
					+ ((registeredProducingRoutes == null) ? 0
							: registeredProducingRoutes.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConnectionContext other = (ConnectionContext) obj;
			if (activeBroker == null) {
				if (other.activeBroker != null)
					return false;
			} else if (!activeBroker.equals(other.activeBroker))
				return false;
			if (connection == null) {
				if (other.connection != null)
					return false;
			} else if (!connection.equals(other.connection))
				return false;
			if (registeredConsumingRoutes == null) {
				if (other.registeredConsumingRoutes != null)
					return false;
			} else if (!registeredConsumingRoutes
					.equals(other.registeredConsumingRoutes))
				return false;
			if (registeredProducingRoutes == null) {
				if (other.registeredProducingRoutes != null)
					return false;
			} else if (!registeredProducingRoutes
					.equals(other.registeredProducingRoutes))
				return false;
			return true;
		}
	}
}
