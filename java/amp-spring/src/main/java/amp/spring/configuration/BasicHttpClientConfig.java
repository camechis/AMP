package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.utility.http.BasicAuthHttpClientProvider;

@Configuration
public class BasicHttpClientConfig implements HttpClientProviderConfig {

	@Value("${hostname}")
	private String hostname;
	
	@Value("${username}")
	private String username;
	
	@Value("${password}")
	private String password;
	
	
	@Override
	@Bean
	public amp.utility.http.HttpClientProvider HttpClientProvider() {
		return new BasicAuthHttpClientProvider(hostname, username, password);
	}

	
	
}
