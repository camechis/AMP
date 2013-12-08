package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.rabbit.connection.BasicConnectionFactory;
import amp.rabbit.connection.IRabbitConnectionFactory;



@Configuration
public class BasicAmpereConnectionConfig implements AmpereConnectionConfig {

	@Value("${rabbit.username:guest}")
	String username;
	
	@Value("${rabbit.password:guest}")
	String password;
		
	@Bean
	@Override
	public IRabbitConnectionFactory ConnectionFactory( ) {
		return new BasicConnectionFactory(username, password);
	}
	
}
