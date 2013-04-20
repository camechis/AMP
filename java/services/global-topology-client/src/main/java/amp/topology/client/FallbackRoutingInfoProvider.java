package amp.topology.client;

import java.util.Map;

import amp.bus.rabbit.topology.RoutingInfo;

public interface FallbackRoutingInfoProvider {

	public RoutingInfo getFallbackRoute(Map<String, String> routingHints);
}
