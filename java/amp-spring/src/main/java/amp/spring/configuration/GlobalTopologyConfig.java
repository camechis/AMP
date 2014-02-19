package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import amp.rabbit.topology.ITopologyService;
import amp.topology.client.DefaultApplicationExchangeProvider;
import amp.topology.client.FallbackRoutingInfoProvider;
import amp.topology.client.GlobalTopologyService;
import amp.topology.client.HttpRoutingInfoRetriever;
import amp.topology.client.IRoutingInfoRetriever;
import amp.utility.serialization.GsonSerializer;

@Configuration
public class GlobalTopologyConfig implements TopologyConfig {
	
	//TODO configure http connection
	@Autowired
	private HttpClientProviderConfig httpConfig;
	
	@Autowired
	private Environment env;
	
	@Override
	@Bean
	public ITopologyService TopologyService() {
		return new GlobalTopologyService(RoutingInfoRetriever(),FallBackProvider());
	}
	
	@Bean
	public IRoutingInfoRetriever RoutingInfoRetriever( ) {
		String urlExpression = env.getProperty("urlExpression");
		return new HttpRoutingInfoRetriever(httpConfig.HttpClientProvider(), urlExpression, new GsonSerializer());
	}
	
	@Bean
	public FallbackRoutingInfoProvider FallBackProvider( ) {
		String hostname = env.getProperty("gts.hostname");
		String port = env.getProperty("gts.port");
		return new DefaultApplicationExchangeProvider(hostname,port);
	}

}
