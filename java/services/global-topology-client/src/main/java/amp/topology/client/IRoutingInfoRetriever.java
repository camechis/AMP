package amp.topology.client;

import java.util.Map;

import amp.bus.rabbit.topology.RoutingInfo;

/**
 * Retrieve routing info for a topic.  We don't care how,
 * just do it!
 * 
 * This is used to allow multiple protocols/implementations
 * for retrieving route info by Global Topology Service.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public interface IRoutingInfoRetriever {

	RoutingInfo retrieveRoutingInfo(Map<String, String> routingHints);
}