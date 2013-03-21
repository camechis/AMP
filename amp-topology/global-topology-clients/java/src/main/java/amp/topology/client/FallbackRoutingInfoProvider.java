package amp.topology.client;

import amp.bus.rabbit.topology.RoutingInfo;

public interface FallbackRoutingInfoProvider {

	public RoutingInfo getFallbackRoute(String topic);
}
