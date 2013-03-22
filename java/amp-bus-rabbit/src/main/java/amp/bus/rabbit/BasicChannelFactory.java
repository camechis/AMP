package amp.bus.rabbit;


import amp.bus.rabbit.topology.Exchange;

import com.rabbitmq.client.ConnectionFactory;


public class BasicChannelFactory extends BaseChannelFactory {
	
	protected String username;
	protected String password;
	
	public BasicChannelFactory(String username, String password) {
		
		super();
		
		this.username = username;
		this.password = password;
	}
	

	@Override
	protected void configureConnectionFactory(ConnectionFactory factory, Exchange exchange) throws Exception {
		
        factory.setUsername(username);
        factory.setPassword(password);

	}

}
