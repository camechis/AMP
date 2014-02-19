package amp.spring.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import amp.bus.DefaultEnvelopeBus;
import amp.bus.ITransportProvider;
import amp.eventing.DefaultEventBus;
import amp.eventing.DefaultRpcBus;
import amp.eventing.RpcFilter;
import amp.messaging.IMessageProcessor;
import amp.messaging.JsonSerializationProcessor;
import amp.messaging.OutboundHeadersProcessor;
import amp.rabbit.transport.RabbitTransportProvider;
import amp.utility.serialization.GsonSerializer;
import amp.utility.serialization.ISerializer;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventBus;
import cmf.eventing.patterns.rpc.IRpcEventBus;

@Configuration
public class AmpereConfig {

	@Autowired
	AmpereConnectionConfig connectionConfig;

	@Autowired
	TopologyConfig topoConfig;

	@Autowired
	RoutingInfoConfig routingInfoConfig;
	
	@Autowired
	AmpereConfigurer configurer;


	@Bean
	public IMessageProcessor JsonEventSerializer() {
		return new JsonSerializationProcessor();
	}

	@Bean
	public IMessageProcessor rpcFilter() {
		return new RpcFilter();
	}

	@Bean(name = "rpcEventBus")
	public IRpcEventBus rpcEventBus() {
		return new DefaultRpcBus(envelopeBus(), InBoundProcessors(),
				OutBoundProcessors());
	}

	@Bean(name = "eventBus")
	public IEventBus EventBus() {
		return new DefaultEventBus(envelopeBus(), InBoundProcessors(),
				OutBoundProcessors());
	}

	@Bean
	public IEnvelopeBus envelopeBus() {
		return new DefaultEnvelopeBus(TransportProvider());
	}

	@Bean
	public ITransportProvider TransportProvider() {
		return new RabbitTransportProvider(topoConfig.TopologyService(),
				connectionConfig.ConnectionFactory(),
				routingInfoConfig.routingInfoCache());
	}

	@Bean
	public IMessageProcessor OutBoundHeaderProcessor() {
		return new OutboundHeadersProcessor();
	}

	@Bean
	public List<IMessageProcessor> InBoundProcessors() {
		List<IMessageProcessor> inBoundProcessors = new ArrayList<IMessageProcessor>();
		inBoundProcessors.add(JsonEventSerializer());
		inBoundProcessors.add(rpcFilter());
		if( configurer != null ) {
			configurer.configureInBoundProcessors(inBoundProcessors);
		}
		return inBoundProcessors;
	}

	@Bean
	public List<IMessageProcessor> OutBoundProcessors() {
		List<IMessageProcessor> outBoundProcessors = new ArrayList<IMessageProcessor>();
		outBoundProcessors.add(JsonEventSerializer());
		outBoundProcessors.add(OutBoundHeaderProcessor());
		outBoundProcessors.add(rpcFilter());
		if( configurer != null ) {
			configurer.configureOutBoundProcessors(outBoundProcessors);
		}
		return outBoundProcessors;
	}
}
