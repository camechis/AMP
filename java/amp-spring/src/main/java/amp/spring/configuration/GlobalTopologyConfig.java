package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.rabbit.topology.ITopologyService;
import amp.topology.client.DefaultApplicationExchangeProvider;
import amp.topology.client.FallbackRoutingInfoProvider;
import amp.topology.client.GlobalTopologyService;
import amp.topology.client.HttpRoutingInfoRetriever;
import amp.topology.client.IRoutingInfoRetriever;
import amp.utility.serialization.GsonSerializer;

@Configuration
public class GlobalTopologyConfig implements TopologyConfig {

	@Value("${hostname}")
	private String hostname;
	
	@Value("${port}")
	private String port;
	
	@Autowired
	private HttpClientProviderConfig httpConfig;
	
	@Value("${urlExpression}")
	private String urlExpression;
	
	@Override
	@Bean
	public ITopologyService TopologyService() {
		return new GlobalTopologyService(RoutingInfoRetriever(),FallBackProvider());
	}
	
	@Bean
	public IRoutingInfoRetriever RoutingInfoRetriever( ) {
		return new HttpRoutingInfoRetriever(httpConfig.HttpClientProvider(), urlExpression, new GsonSerializer());
	}
	
	@Bean
	public FallbackRoutingInfoProvider FallBackProvider( ) {
		return new DefaultApplicationExchangeProvider(hostname,port);
	}

}
