package amp.topology.core.repo.inmem;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.model.TopologyModel;
import amp.topology.core.repo.TopologyModelRepository;

public class InMemoryTopologyModelRepository<T extends TopologyModel> 
			 implements TopologyModelRepository<T> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected ConcurrentHashMap<String, T> modelInstances = new ConcurrentHashMap<String, T>();
	
	@Override
	public boolean create(T model) {
		
		logger.debug("Creating {} with id: {}", model.getClass().getName(), model.getId());
		
		return null == this.modelInstances.putIfAbsent(model.getId(), model);
	}

	@Override
	public boolean update(T model) {
		
		logger.debug("Updating {} with id: {}", model.getClass().getName(), model.getId());
		
		return null != this.modelInstances.replace(model.getId(), model);
	}

	@Override
	public boolean delete(String modelId) {
		
		logger.debug("Deleting model with id: {}", modelId);
		
		return null != this.modelInstances.remove(modelId);
	}

	@Override
	public T get(String modelId) {
		
		logger.debug("Getting model with id: {}", modelId);
		
		return this.modelInstances.get(modelId);
	}

	@Override
	public Collection<T> all() {
		
		logger.debug("Getting all model instances.");
		
		return this.modelInstances.values();
	}
	
	public void setModelInstances(Collection<T> instances){
		
		for (T instance : instances){
			
			this.modelInstances.put(instance.getId(), instance);
		}
	}
}
