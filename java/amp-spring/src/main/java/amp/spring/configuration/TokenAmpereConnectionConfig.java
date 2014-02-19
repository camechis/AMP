package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import amp.rabbit.connection.CertificateConnectionFactory;
import amp.rabbit.connection.IRabbitConnectionFactory;
import amp.rabbit.connection.TokenConnectionFactory;
import amp.utility.http.HttpClientProvider;
import amp.utility.serialization.GsonSerializer;

@Configuration
public class TokenAmpereConnectionConfig implements AmpereConnectionConfig {

	@Autowired
	HttpClientProviderConfig httpClientProvider;

	@Autowired
	AmpereConnectionConfig connectionConfig;

	@Autowired
	Environment env;

	@Autowired
	AmpereConfigurer configurer;

	@Override
	@Bean
	public IRabbitConnectionFactory ConnectionFactory() {
		String anubisUri = env.getProperty("anubisUri");
		HttpClientProvider clientProvider = null;
		if (configurer != null) {
			clientProvider = configurer.configureAnubisConnection();
		}
		if (clientProvider == null) {
			throw new RuntimeException(
					"An HttpClient Connection must supplied in order to use a the Token Connection, see AmpereConfigurer");
		}
		return new TokenConnectionFactory(
				httpClientProvider.HttpClientProvider(), anubisUri,
				new GsonSerializer(),
				(CertificateConnectionFactory) connectionConfig
						.ConnectionFactory());

	}

}
