package amp.rabbit.connection;

import java.util.concurrent.ConcurrentHashMap;

import cmf.bus.IDisposable;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.topology.Exchange;

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

    protected Logger log;

	public static int HEARTBEAT_INTERVAL = 2;

	protected ConcurrentHashMap<Exchange, ConnectionManager> pooledManagers = new ConcurrentHashMap<Exchange, ConnectionManager>();
	
	/**
	 * Create a new instance of the ChannelFactory using the "SameBrokerStrategy"
	 */
	public BaseChannelFactory() {
        log = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * Convenience method to allow setting of the heartbeat interval via DI container.
	 * @param interval 
	 */
	public void setHeartbeatInterval(int interval){
		HEARTBEAT_INTERVAL = interval;
	}
	
	private ConnectionManager createConnectionManager(Exchange exchange) throws Exception {

        log.debug("Creating connection manager for exchange: {}", exchange.toString());

		ConnectionFactory factory = new ConnectionFactory();

		configureConnectionFactory(factory, exchange);
		
        return new ConnectionManager(factory);
	}
	
	/**
	 * Derived classes have the sole responsibility of configuring the RabbitConnectionFactory.
	 * (however that is done: username/password, certificates, etc.).
	 */
	public void configureConnectionFactory(ConnectionFactory factory, Exchange exchange)  throws Exception {

        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setVirtualHost(exchange.getVirtualHost());
	}
	
	/**
	 * Get the corresponding channel for the supplied Exchange.
	 * This overload specifically pools channels depending on the 
	 * ChannelEqualityStrategy. 
	 * @param exchange Exchange configuration for the Channel
	 * @return an AMQP Channel
	 */
	@Override
	public synchronized ConnectionManager getConnectionFor(Exchange exchange) throws Exception {
		
		log.trace("Getting connection manager for exchange: {}", exchange);
		
		ConnectionManager manager = null;
		
		if (pooledManagers.containsKey(exchange)){
			
			manager = pooledManagers.get(exchange);
		}
		else {
			
			manager = this.createConnectionManager(exchange);
			
			pooledManagers.put(exchange, manager);
		}
			
		//TODO: Fixing soon...
		//connection.addShutdownListener(new RabbitConnectionShutdownListener(this, exchange));
			
		return manager;
	}
	
	/**
	 * Remove the connection from the pool.
	 * @param exchange Exchange of the active connection.
	 * @return True if it was successfully removed.
	 */
	public boolean removeConnection(Exchange exchange){
		
		ConnectionManager connection = pooledManagers.remove(exchange);
		
		return connection != null;
	}
	
	/**
	 * Iterate over pooled connections, closing each connection.
	 */
	@Override
	public void dispose() {
		
		for (ConnectionManager connection : this.pooledManagers.values()){
				
			connection.dispose();
				
		}
	}
}
