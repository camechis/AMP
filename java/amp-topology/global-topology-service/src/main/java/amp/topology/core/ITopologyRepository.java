package amp.topology.core;

import java.util.List;

public interface ITopologyRepository {
	
	ExtendedExchange getExchange(String id);
	
	ExtendedRouteInfo getRoute(String id);
	
	List<ExtendedExchange> getExchanges();
	
	List<ExtendedRouteInfo> getRoutes();
	
	void createExchange(ExtendedExchange exchange);
	
	void createRoute(ExtendedRouteInfo routeInfo);
	
	void updateExchange(ExtendedExchange exchange);
	
	void updateRoute(ExtendedRouteInfo routeInfo);
	
	boolean removeExchange(String id);
	
	boolean removeRoute(String id);
	
	void purge();
	
	List<ExtendedRouteInfo> find(String topic, String client);
	
	void addEventListener(ITopologyRepositoryEventListener listener);
	
	void removeEventListener(ITopologyRepositoryEventListener listener);
}
