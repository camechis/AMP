package amp.topology.core.factory.dynamic.repo.inmem;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.definitions.TopologyDefinition;

public class InMemoryDefinitionRepository<T extends TopologyDefinition> 
		implements DefinitionRepository<T>  {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected ConcurrentHashMap<String, T> definitions = new ConcurrentHashMap<String, T>();
	
	@Override
	public boolean create(T definition) {
		
		logger.debug("Creating definition with ID: {}", definition.getId());
		
		T oldValue = this.definitions.putIfAbsent(definition.getId(), definition);
		
		return oldValue == null;
	}

	@Override
	public boolean update(T definition) {
		
		logger.debug("Updating definition with ID: {}", definition.getId());
		
		T oldValue = this.definitions.replace(definition.getId(), definition);
		
		return oldValue != null;
	}

	@Override
	public T get(String id) {
		
		logger.debug("Getting definition with ID: {}", id);
		
		return this.definitions.get(id);
	}

	@Override
	public Collection<T> all() {
		
		logger.debug("Getting all definitions.");
		
		return this.definitions.values();
	}

	@Override
	public boolean delete(String id) {
		
		logger.debug("Deleting definition with ID: {}", id);
		
		T itemToRemove = this.definitions.remove(id);
		
		return itemToRemove != null;
	}
	
	public void setDefinitions(Collection<T> definitions){
		
		logger.debug("Adding {} definitions to repository.", definitions.size());
		
		for (T definition : definitions){
			
			logger.debug("Adding definition: {}", definition.getId());
			
			this.definitions.put(definition.getId(), definition);
		}
	}
	
}