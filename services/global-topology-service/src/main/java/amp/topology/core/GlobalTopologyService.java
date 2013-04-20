package amp.topology.core;

import java.util.Map;

import amp.bus.rabbit.topology.ITopologyService;
import amp.bus.rabbit.topology.RoutingInfo;

public class GlobalTopologyService implements ITopologyService {

	

	@Override
	public RoutingInfo getRoutingInfo(Map<String, String> routingHints) {
		
		return null;
	}

	@Override
	public void dispose() {}
	
}
