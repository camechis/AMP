package amp.topology.resources;

import amp.topology.core.ExtendedRouteInfo;

public class RouteInfoWrapper {

	private ExtendedRouteInfo routeInfo = null;
	
	public RouteInfoWrapper(ExtendedRouteInfo routeInfo) {
		this.routeInfo = routeInfo;
	}

	public ExtendedRouteInfo getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(ExtendedRouteInfo routeInfo) {
		this.routeInfo = routeInfo;
	}
}
