package amp.topology.core.repo.inmem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Predicates;

import amp.topology.core.BaseTopologyRepository;
import amp.topology.core.ExtendedExchange;
import amp.topology.core.ExtendedRouteInfo;
import amp.topology.core.ITopologyRepository;
import amp.topology.core.ITopologyRepositoryEventListener;

/**
 * The simplest Repository one could use (keeping everything in memory).
 * This implementation is only recommended for use in development.  You can
 * make the state persistent by pairing the Repository with the TopologySnapshot
 * facility.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class InMemoryTopologyRepository extends BaseTopologyRepository implements ITopologyRepository {

	ConcurrentHashMap<String, ExtendedExchange> exchanges = 
			new ConcurrentHashMap<String, ExtendedExchange>();
	
	ConcurrentHashMap<String, ExtendedRouteInfo> routes = 
			new ConcurrentHashMap<String, ExtendedRouteInfo>();
	
	public InMemoryTopologyRepository(){}
	
	public InMemoryTopologyRepository(
			Collection<ITopologyRepositoryEventListener> listeners) {
		
		super(listeners);
	}

	public InMemoryTopologyRepository(
		Collection<ExtendedExchange> exchanges, Collection<ExtendedRouteInfo> routes){
		
		super();
		
		setExchanges(exchanges);
		setRoutes(routes);
	}
	
	public InMemoryTopologyRepository(
			Collection<ITopologyRepositoryEventListener> listeners,
			ConcurrentHashMap<String, ExtendedExchange> exchanges,
			ConcurrentHashMap<String, ExtendedRouteInfo> routes) {
		
		super(listeners);
		
		this.exchanges = exchanges;
		this.routes = routes;
	}
	
	public void setExchanges(Collection<ExtendedExchange> exchangesToSet){
		
		for (ExtendedExchange exchange : exchangesToSet){
			
			exchanges.put(exchange.getId(), exchange);
		}
	}
	
	public void setRoutes(Collection<ExtendedRouteInfo> routesToSet){
		
		for (ExtendedRouteInfo route : routesToSet){
			
			routes.put(route.getId(), route);
		}
	}
	
	@Override
	public ExtendedExchange getExchange(String id) {
		
		return exchanges.get(id);
	}

	@Override
	public ExtendedRouteInfo getRoute(String id) {
		
		return routes.get(id);
	}

	@Override
	public List<ExtendedExchange> getExchanges() {
		
		return new ArrayList<ExtendedExchange>(exchanges.values());
	}

	@Override
	public List<ExtendedExchange> getExchangesByBroker(String host) {
		
		List<ExtendedExchange> matchingExchanges = new ArrayList<ExtendedExchange>();
		
		return matchingExchanges;
	}

	@Override
	public List<ExtendedExchange> getExchangesByBroker(String host, int port) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExtendedExchange> getExchangesByBroker(String host, int port,
			String vhost) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<ExtendedRouteInfo> getRoutes() {
		
		return new ArrayList<ExtendedRouteInfo>(routes.values());
	}

	@Override
	public List<ExtendedRouteInfo> getRoutesByTopic(String topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExtendedRouteInfo> getRoutesByClient(String client) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void createExchange(ExtendedExchange exchange) {
		
		exchanges.put(exchange.getId(), exchange);
		
		fireExchangeCreated(exchange);
	}

	@Override
	public void createRoute(ExtendedRouteInfo routeInfo) {
		
		routes.put(routeInfo.getId(), routeInfo);
		
		fireRouteCreated(routeInfo);
	}

	@Override
	public void updateExchange(ExtendedExchange exchange) {
		
		ExtendedExchange oldExchange = exchanges.replace(exchange.getId(), exchange);
		
		if (oldExchange != null){
			
			fireExchangeUpdated(oldExchange, exchange);
		}
	}

	@Override
	public void updateRoute(ExtendedRouteInfo routeInfo) {
		
		ExtendedRouteInfo oldRouteInfo = routes.replace(routeInfo.getId(), routeInfo);
		
		if (oldRouteInfo != null){
			
			fireRouteUpdated(oldRouteInfo, routeInfo);
		}
	}

	@Override
	public boolean removeExchange(String id) {
		
		ExtendedExchange exchange = exchanges.remove(id);
		
		if (exchange != null){
			
			fireExchangeRemoved(exchange);
		}
		
		return exchange != null;
	}

	@Override
	public boolean removeRoute(String id) {
		
		ExtendedRouteInfo routeInfo = routes.remove(id);
		
		if (routeInfo != null){
			
			fireRouteRemoved(routeInfo);
		}
		
		return routeInfo != null;
	}

	@Override
	public List<ExtendedRouteInfo> find(String topic, String client) {
		
		ArrayList<ExtendedRouteInfo> matchingRoutes = new ArrayList<ExtendedRouteInfo>();
		
		for (ExtendedRouteInfo route : routes.values()){
			
			if (route.getClients().contains(client) && route.getTopics().contains(topic)){
				
				matchingRoutes.add(route);
			}
		}
		
		fireRoutingInfoRetrieved(topic, client, matchingRoutes);
		
		return matchingRoutes;
	}

	@Override
	public void purge() {
		
		this.exchanges.clear();
		this.routes.clear();
	}
}