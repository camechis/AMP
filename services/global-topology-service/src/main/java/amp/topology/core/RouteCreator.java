package amp.topology.core;

import java.util.Collection;

import amp.bus.rabbit.topology.RouteInfo;

public interface RouteCreator {
	
	void create(Collection<RouteInfo> routeInfos, TopologyRequestContext context);
}