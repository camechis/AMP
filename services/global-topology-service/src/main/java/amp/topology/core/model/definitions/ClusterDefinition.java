package amp.topology.core.model.definitions;

import amp.topology.core.model.TopologyModel;

public class ClusterDefinition extends TopologyModel implements TopologyDefinition {

	protected String clusterIdExpression;

	public ClusterDefinition(){}
	
	public ClusterDefinition(
			String id, String description,
			String clusterIdExpression) {
		
		super(id, description);
		
		this.clusterIdExpression = clusterIdExpression;
	}

	public String getClusterIdExpression() {
		return clusterIdExpression;
	}
	
}
