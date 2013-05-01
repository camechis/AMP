package amp.topology.core.factory.index.selection;

import java.util.List;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.factory.index.RoutingInfoSelectionContext;

public interface RoutingInfoSelectionStrategy {
	
	FactoryReference<RoutingInfo> selectBestMatch(
			List<RoutingInfoSelectionContext> routeOptions);
}
