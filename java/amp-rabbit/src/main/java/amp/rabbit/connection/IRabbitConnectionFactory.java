package amp.rabbit.connection;


import java.util.Collection;

import cmf.bus.IDisposable;


import amp.rabbit.topology.ConsumingRoute;
import amp.rabbit.topology.ProducingRoute;


/**
 * Factory that creates the managers for connection+channel generation 
 * to the various route(s) needed by the client objects.
 *
 * @author jmccune
 */
public interface IRabbitConnectionFactory extends IDisposable {

	Collection<IConnectionManager> getConnectionManagersFor(ProducingRoute route) throws Exception;
	Collection<IConnectionManager> getConnectionManagersFor(ConsumingRoute route) throws Exception;
}
