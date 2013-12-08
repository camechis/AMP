package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.rabbit.connection.CertificateConnectionFactory;
import amp.rabbit.connection.IRabbitConnectionFactory;
import amp.rabbit.connection.TokenConnectionFactory;
import amp.utility.serialization.GsonSerializer;

@Configuration
public class TokenAmpereConnectionConfig implements AmpereConnectionConfig {

	@Value("${anubisUri}")
	private String anubisUri;

	@Autowired
	HttpClientProviderConfig httpClientProvider;

	@Autowired
	AmpereConnectionConfig connectionConfig;

	@Override
	@Bean
	public IRabbitConnectionFactory ConnectionFactory() {

		return new TokenConnectionFactory(
				httpClientProvider.HttpClientProvider(), anubisUri,
				new GsonSerializer(),
				(CertificateConnectionFactory) connectionConfig
						.ConnectionFactory());

	}

}
