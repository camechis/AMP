package amp.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.commanding.DefaultCommandReceiver;
import amp.commanding.ICommandReceiver;
import amp.rabbit.transport.CommandableCache;
import amp.rabbit.transport.IRoutingInfoCache;
import amp.rabbit.transport.RabbitEnvelopeReceiver;
import cmf.bus.IEnvelopeReceiver;

@Configuration
public class CommandableRoutingInfoCache  implements RoutingInfoConfig{

	private Long cacheExpiryInSeconds;
	
	private AmpereConnectionConfig connectionConfig;
	
	@Autowired
	private TopologyConfig topoConfig;
	
	@Override
	@Bean
	public IRoutingInfoCache routingInfoCache() {
		return new CommandableCache( CommandReceiver(), cacheExpiryInSeconds);
	}
	
	@Bean
	public ICommandReceiver CommandReceiver( ) {
		return new DefaultCommandReceiver(envelopeReceiver());
	}
	
	@Bean
	public IEnvelopeReceiver envelopeReceiver(  ) {
		return new RabbitEnvelopeReceiver(topoConfig.TopologyService(), connectionConfig.ConnectionFactory());
	}

}
