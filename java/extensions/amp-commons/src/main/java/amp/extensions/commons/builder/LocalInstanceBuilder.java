package amp.extensions.commons.builder;

import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventBus;
import cmf.eventing.patterns.rpc.IRpcEventBus;

/**
 * Assuming you've used the default configuration for EVERYTHING,
 * you should be able to instantiate a Bus with the default config
 * in a single method call.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class LocalInstanceBuilder {
	
	/**
	 * Get an Envelope Bus.
	 * @return Envelope Bus.
	 */
	public IEnvelopeBus envelopeBus(){
		
		return new BusBuilder()
				.configure()
					.connectWith()
						.basicAuth("guest", "guest")
					.routeWith()
						.simpleTopology()
							.broker("localhost")
							.exchange("amp.direct")
				.and()
				.envelopeBus().getInstance();
	}
	
	/**
	 * Get an Event Bus.
	 * @return Event Bus.
	 */
	public IEventBus eventBus(){
		
		return new BusBuilder()
				.configure()
					.connectWith()
						.basicAuth("guest", "guest")
					.routeWith()
						.simpleTopology()
							.broker("localhost")
							.exchange("amp.direct")
				.and()
				.eventBus().getInstance();
	}
	
	/**
	 * Get an RPC-capable Event Bus.
	 * @return RPC Event Bus.
	 */
	public IRpcEventBus rpcEventBus(){
		
		return new BusBuilder()
				.configure()
					.connectWith()
						.basicAuth("guest", "guest")
					.routeWith()
						.simpleTopology()
							.broker("localhost")
							.exchange("amp.direct")
				.and()
				.eventBus().getRpcBusInstance();
	}
}
