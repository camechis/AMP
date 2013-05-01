package amp.topology.core;

import java.util.Map;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.model.Client;

public interface RoutingInfoController {

	RoutingInfo getRouteFromContext(Client client, Map<String, String> context);
	
}
