package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.rabbit.transport.IRoutingInfoCache;
import amp.rabbit.transport.SimpleRoutingInfoCache;

@Configuration
public class SimpleRoutingInfoConfig  implements RoutingInfoConfig {

	@Value("${cacheExpiryInSeconds}")
	private Long cacheExpiryInSeconds;
	
	@Override
	@Bean
	public IRoutingInfoCache routingInfoCache() {
		return new SimpleRoutingInfoCache(cacheExpiryInSeconds);
	}

}
