package amp.rabbit.connection;


import cmf.bus.IDisposable;

import amp.rabbit.topology.Exchange;


public interface IRabbitChannelFactory extends IDisposable {

	ConnectionManager getConnectionFor(Exchange exchange) throws Exception;
}
