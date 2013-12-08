package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.rabbit.connection.CertificateConnectionFactory;
import amp.rabbit.connection.IRabbitConnectionFactory;

@Configuration
public class HttpClientConfig {

	
	@Value("${keystore}")
	private String keystore = null;

	@Value("${keystorePassword}")
	private String keystorePassword = null;

	@Value("${truststore}")
	private String trustStore = null;

	@Bean
	public IRabbitConnectionFactory ConnectionFactory() {
		return new CertificateConnectionFactory(keystore, keystorePassword,
				trustStore);
	}
	
	
}
