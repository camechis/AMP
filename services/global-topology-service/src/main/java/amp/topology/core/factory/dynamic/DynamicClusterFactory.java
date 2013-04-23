package amp.topology.core.factory.dynamic;

import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.model.definitions.ClusterDefinition;
import amp.topology.core.repo.ClusterRepository;

public class DynamicClusterFactory extends EvaluatingChainedTopologyFactory<Cluster> {

	DefinitionRepository<ClusterDefinition> definitionRepository;
	ClusterRepository clusterRepository;
	
	public DynamicClusterFactory(
			ExpressionEvaluator evaluator, 
			DefinitionRepository<ClusterDefinition> definitionRepository,
			ClusterRepository clusterRepository) {
		
		super(evaluator);
		
		this.definitionRepository = definitionRepository;
		this.clusterRepository = clusterRepository;
	}

	@Override
	public Cluster build(String context) {
		
		ClusterDefinition definition = this.definitionRepository.get(context);
		
		String clusterId = this.evaluate(definition.getClusterIdExpression(), String.class);
		
		Cluster cluster = null;
		
		try {
			
			cluster = this.clusterRepository.get(clusterId);
			
		} catch (ClusterDoesntExistException e){
			
			logger.error("Cluster not found! {}", e);
		}
		
		return cluster;
	}

}
