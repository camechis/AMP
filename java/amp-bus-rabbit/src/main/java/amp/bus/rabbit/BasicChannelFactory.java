package amp.bus.rabbit;


import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.BaseRoute;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;


public class BasicChannelFactory extends BaseChannelFactory {

    protected Logger log;
	protected String username;
	protected String password;
	
	public BasicChannelFactory(String username, String password) {
		
		super();

        log = LoggerFactory.getLogger(this.getClass());
		this.username = username;
		this.password = password;
	}
	
	
	Connection createConnection(Broker broker, BaseRoute route) throws IOException {

		ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setHost(broker.getHostname());
        factory.setPort(broker.getPort());
        factory.setVirtualHost(route.getExchange().getVirtualHost());
        return factory.newConnection();
	}


	@Override
	public ConnectionContext getConnection(Broker broker, ProducingRoute route)
			throws Exception {
		
		Connection connection = createConnection(broker, route);
		
		return new ConnectionContext(broker, route, connection);
	}


	@Override
	public ConnectionContext getConnection(Broker broker, ConsumingRoute route)
			throws Exception {
		
		Connection connection = createConnection(broker, route);
		
		return new ConnectionContext(broker, route, connection);
	}

	
}
