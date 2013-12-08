package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.rabbit.topology.Broker;
import amp.rabbit.topology.ITopologyService;
import amp.rabbit.topology.SimpleTopologyService;

@Configuration
public class SimpleTopologyConfig implements TopologyConfig {

	@Value("${clientProfile}")
	String clientProfile;

	@Value("${rabbit.hostname}")
	String hostname;
	
	@Value("${rabbbit.port:5671}")
	Integer port;
	
	@Bean
	@Override
	public ITopologyService TopologyService( ) {
		Broker broker = new Broker(hostname,port);
		return new SimpleTopologyService(clientProfile,broker);
	}

}
