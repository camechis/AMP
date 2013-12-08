package amp.spring.configuration;

import amp.rabbit.connection.IRabbitConnectionFactory;

public interface AmpereConnectionConfig {
	public IRabbitConnectionFactory ConnectionFactory( );
}
