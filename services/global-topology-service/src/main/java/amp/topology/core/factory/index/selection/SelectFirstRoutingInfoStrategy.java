package amp.topology.core.factory.index.selection;

import java.util.List;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.factory.index.RoutingInfoSelectionContext;

public class SelectFirstRoutingInfoStrategy implements RoutingInfoSelectionStrategy {

	@Override
	public FactoryReference<RoutingInfo> selectBestMatch(
			List<RoutingInfoSelectionContext> routeOptions) {
		
		if (routeOptions.size() > 0){
		
			return routeOptions.get(0).getRoutingInfo();
		}
		return null;
	}

}
