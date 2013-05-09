package amp.extensions.commons;

import static org.junit.Assert.*;

import org.junit.Test;

import cmf.eventing.IEventBus;

import amp.extensions.commons.builder.BusBuilder;

public class IntegrationExample {

	@Test
	public void instantiate_event_bus_via_fluent_api() throws Exception {
		
		IEventBus eventBus = 
			new BusBuilder()
				.configure()
					.connectWith()
						.basicAuth("guest", "guest")
					.routeWith()
						.globalTopology()
							.usingHttp("localhost", 15677, "app01", "password")
				.and().eventBus().getInstance();
			    .and().commandChannel().getInstance();

		eventBus.publish(new String("Hello"));
		
		IEventBus eventBus2 = BusBuilder.localProfile().eventBus();
	}
}