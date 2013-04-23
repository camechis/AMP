package amp.topology.core.factory.dynamic.repo.inmem;

import amp.topology.core.model.definitions.ClusterDefinition;

/**
 * Only here because Spring can't instantiate a Generic type.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class InMemoryClusterDefinitionRepository 
	extends InMemoryDefinitionRepository<ClusterDefinition> {}
