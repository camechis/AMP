package amp.topology.core.repo;

import java.util.Collection;

import amp.topology.core.model.TopologyModel;

public interface TopologyModelRepository<T extends TopologyModel> {

	boolean create(T model);
	
	boolean update(T model);
	
	boolean delete(String modelId);
	
	T get(String modelId);
	
	Collection<T> all();
	
}
