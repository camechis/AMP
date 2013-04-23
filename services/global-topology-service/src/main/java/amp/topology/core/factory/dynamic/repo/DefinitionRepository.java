package amp.topology.core.factory.dynamic.repo;

import java.util.Collection;

import amp.topology.core.model.definitions.TopologyDefinition;

public interface DefinitionRepository<T extends TopologyDefinition> {

	boolean create(T definition);
	
	boolean update(T definition);
	
	T get(String id);
	
	Collection<T> all();
	
	boolean delete(String id);
}
