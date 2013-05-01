package amp.topology.core.factory;

import java.util.List;
import java.util.Map;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.RoutingInfoController;
import amp.topology.core.factory.index.RoutingInfoIndex;
import amp.topology.core.factory.index.RoutingInfoSelectionContext;
import amp.topology.core.factory.index.selection.RoutingInfoSelectionStrategy;
import amp.topology.core.model.Client;

public class FactoryAssemblingRoutingInfoController implements RoutingInfoController {

	RoutingInfoIndex routingInfoIndex;
	RoutingInfoSelectionStrategy selectionStrategy;
	DelegatingModelFactory modelFactory;
	
	public FactoryAssemblingRoutingInfoController(
			RoutingInfoIndex routingInfoIndex,
			RoutingInfoSelectionStrategy selectionStrategy,
			DelegatingModelFactory modelFactory) {
		
		this.routingInfoIndex = routingInfoIndex;
		this.selectionStrategy = selectionStrategy;
		this.modelFactory = modelFactory;
	}
	
	@Override
	public RoutingInfo getRouteFromContext(Client client, Map<String, String> context) {
		
		List<RoutingInfoSelectionContext> routeOptions =
				routingInfoIndex.getMatches(client, context);
		
		if (routeOptions != null && routeOptions.size() > 0){
			
			FactoryReference<RoutingInfo> selectedRoute = 
					selectionStrategy.selectBestMatch(routeOptions);
			
			if (selectedRoute != null){
				
				return modelFactory.make(selectedRoute);
			}
		}
		
		return null;
	}

}
