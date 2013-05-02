package amp.topology.core.factory.index;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import amp.topology.core.model.Client;


public interface RoutingInfoIndex {
	
	boolean create(RoutingInfoSelectionContext context);
	
	boolean update(RoutingInfoSelectionContext context);
	
	boolean delete(String id);
	
	RoutingInfoSelectionContext get(String id);
	
	List<RoutingInfoSelectionContext> all();
	
	List<RoutingInfoSelectionContext> getMatches(
			Client client,
			Map<String, String> routingContext);
	
	Collection<String> topics(String filter);
	
	Collection<String> messagePatterns(String filter);
	
	Collection<String> clients(String filter);
	
	Collection<String> groups(String filter);
}