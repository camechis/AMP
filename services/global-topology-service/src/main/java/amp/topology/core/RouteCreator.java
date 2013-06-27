package amp.topology.core;

import amp.rabbit.topology.RouteInfo;

import java.util.Collection;



public interface RouteCreator {
	
	void create(Collection<RouteInfo> routeInfos, TopologyRequestContext context);
}