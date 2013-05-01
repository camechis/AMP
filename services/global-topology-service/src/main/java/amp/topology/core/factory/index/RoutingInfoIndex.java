package amp.topology.core.factory.index;

import java.util.List;
import java.util.Map;

import amp.topology.core.model.Client;


public interface RoutingInfoIndex {
	
	List<RoutingInfoSelectionContext> getMatches(
			Client client,
			Map<String, String> routingContext);
}