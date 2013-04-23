package amp.topology.core.model.definitions;

import java.util.ArrayList;
import java.util.Collection;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.TopologyModel;

public class SubtopologyDefinition extends TopologyModel implements TopologyDefinition {

	ArrayList<FactoryReference<RoutingInfo>> definitions = new ArrayList<FactoryReference<RoutingInfo>>();
	
	public SubtopologyDefinition(){}
	
	public SubtopologyDefinition(
			String id, String description,
			Collection<FactoryReference<RoutingInfo>> definitions) {
		
		super(id, description);
		this.definitions.addAll(definitions);
	}
	
	public Collection<FactoryReference<RoutingInfo>> getDefinitions() {
		return definitions;
	}
}