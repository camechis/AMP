package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.utility.http.SslHttpClientProvider;

@Configuration
public class SSLHttpClientProviderConfig implements HttpClientProviderConfig{

	@Value("${keystore}")
	private String keystore;
	
	@Value("${keystorePassword}")
	private String keystorePassword;
	
	
	@Override
	@Bean
	public amp.utility.http.HttpClientProvider HttpClientProvider() {
		return new SslHttpClientProvider(keystore, keystorePassword);
	}

}
