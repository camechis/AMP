package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import amp.rabbit.connection.BasicConnectionFactory;
import amp.rabbit.connection.IRabbitConnectionFactory;

@Configuration
public class BasicAmpereConnectionConfig implements AmpereConnectionConfig {

	@Autowired
	Environment env;

	@Bean
	@Override
	public IRabbitConnectionFactory ConnectionFactory() {
		String username = env.getProperty("rabbit.username") != null ? env
				.getProperty("rabbit.username") : "guest";
		String password = env.getProperty("rabbit.password") != null ? env
				.getProperty("rabbit.password") : "guest";
		return new BasicConnectionFactory(username, password);
	}

}
