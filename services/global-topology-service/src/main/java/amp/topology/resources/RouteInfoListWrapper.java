package amp.topology.resources;

import java.util.List;

import amp.topology.core.ExtendedRouteInfo;


public class RouteInfoListWrapper {

	private List<ExtendedRouteInfo> routeInfo;

	public RouteInfoListWrapper(List<ExtendedRouteInfo> routeInfos) {
		this.routeInfo = routeInfos;
	}

	public List<ExtendedRouteInfo> getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(List<ExtendedRouteInfo> routeInfos) {
		this.routeInfo = routeInfos;
	}
}
