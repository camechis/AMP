package amp.rabbit.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import cmf.bus.IDisposable;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.topology.Broker;
import amp.rabbit.topology.BaseRoute;
import amp.rabbit.topology.ConsumingRoute;
import amp.rabbit.topology.ProducingRoute;

/**
 * Provides a lot of the boiler-plate functionality most Rabbit Connection Factories
 * would need to function properly. This base implementation imposes only one responsibility
 * on deriving classes: that they create and supply the connection to the broker (which
 * is where most configuration like authentication is done). 
 *  
 * @author Richard Clayton (Berico Technologies)
 * @author jmccune (Berico Technologies)
 *
 */
public abstract class BaseConnectionFactory implements IRabbitConnectionFactory, IDisposable {

    protected Logger log;

	public ConnectionReuseStrategy consumerReuseStrategy = ConnectionReuseStrategy.SAME_BROKER;
	public ConnectionReuseStrategy producerReuseStrategy = ConnectionReuseStrategy.SAME_BROKER;
		
	protected ConcurrentHashMap<Broker, IConnectionManager> pooledManagers =
			new ConcurrentHashMap<Broker, IConnectionManager>();
	
	/**
	 * Create a new instance of the ConnectionFactory using the "SameBrokerStrategy"
	 */
	public BaseConnectionFactory() {
        log = LoggerFactory.getLogger(this.getClass());
	}

	
	@Override
	public Collection<IConnectionManager> getConnectionManagersFor(ProducingRoute route) throws Exception {
		return getConnectionManagers(route);
	}
	
	@Override
	public Collection<IConnectionManager> getConnectionManagersFor(ConsumingRoute route) throws Exception {
		return getConnectionManagers(route);
	}
	
//  TODO: JM Add removeConnection back in later
//	/**
//	 * Remove the connection from the pool.
//	 * @param exchange Exchange of the active connection.
//	 * @return True if it was successfully removed.
//	 */
//	public boolean removeConnection(Exchange exchange){
//		
//		IConnectionManager connection = pooledManagers.remove(exchange);
//		
//		return connection != null;
//	}
	
	/**
	 * Iterate over pooled connections, closing each connection.
	 */
	@Override
	public void dispose() {
		
		for (IConnectionManager connection : this.pooledManagers.values()){
				
			connection.dispose();
				
		}
	}
	
	/**
	 * Get the corresponding channel for the supplied Exchange.
	 * This overload specifically pools channels depending on the 
	 * ChannelEqualityStrategy. 
	 * @param exchange Exchange configuration for the Channel
	 * @return an AMQP Channel
	 */	
	protected synchronized Collection<IConnectionManager> getConnectionManagers(BaseRoute route) throws Exception {
		
		
		Collection<IConnectionManager> managers = new ArrayList<IConnectionManager>();
		Collection<Broker> brokers = route.getBrokers();
		
		for (Broker broker : brokers) {
			log.trace("Getting connection manager for exchange: {}", broker);
			
			IConnectionManager manager = null;
			if (pooledManagers.containsKey(broker)){
				
				manager = pooledManagers.get(broker);
			}
			else {
				
				manager = this.createConnectionManager(broker,route);
				
				pooledManagers.put(broker, manager);
			}
			managers.add(manager);
			//TODO: Fixing soon...
			//connection.addShutdownListener(new RabbitConnectionShutdownListener(this, exchange));
		}
		
			
		return managers;
	}
	

	/**
	 * Derived classes have the sole responsibility of configuring the RabbitConnectionFactory.
	 * (however that is done: username/password, certificates, etc.).
	 */
	protected void configureConnectionFactory(ConnectionFactory factory, Broker broker, BaseRoute route) 
			throws Exception {

        factory.setHost(broker.getHostname());
        factory.setPort(broker.getPort());
        factory.setVirtualHost(route.getExchange().getVirtualHost());
	}
	

	/**
	 * Convenience method for DRY principle -- absolutely no difference between how we 
	 * create a connection for a ConsumingRoute or a ProducingRoute.  Just use the BaseRoute.
	 * @param broker
	 * @param route
	 * @return
	 * @throws Exception
	 */
	protected IConnectionManager createConnectionManager(Broker broker, BaseRoute route) throws Exception {
		
        log.debug("Creating connection manager for exchange: {}", broker);

		ConnectionFactory factory = new ConnectionFactory();

		configureConnectionFactory(factory, broker, route);
		
        return new ConnectionManager(factory);
	}
}
