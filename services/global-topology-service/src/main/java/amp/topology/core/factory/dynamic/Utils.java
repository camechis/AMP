package amp.topology.core.factory.dynamic;

import java.util.ArrayList;
import java.util.Collection;

import amp.topology.core.model.RoutingContext;

public class Utils {

	public static Collection<String> asRoutingKeys(
			Collection<RoutingContext> routingContexts){
		
		ArrayList<String> routingKeys = new ArrayList<String>();
		
		for (RoutingContext context : routingContexts){
			
			routingKeys.add(context.getValue());
		}
		
		return routingKeys;
	}
	
}
