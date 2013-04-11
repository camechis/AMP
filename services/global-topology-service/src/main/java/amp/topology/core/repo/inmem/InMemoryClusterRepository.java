package amp.topology.core.repo.inmem;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.model.Cluster;
import amp.topology.core.model.ClusterRepository;

/**
 * In-Memory implementation of the ClusterRepository.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class InMemoryClusterRepository implements ClusterRepository {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryClusterRepository.class);
	
	protected ConcurrentHashMap<String, Cluster> clusters = new ConcurrentHashMap<String, Cluster>();
	
	
	public void setClusters(Collection<Cluster> clusters){
		
		for (Cluster cluster : clusters){
			
			this.clusters.put(cluster.getId(), cluster);
		}
	}
	
	
	@Override
	public Cluster get(String clusterId) {
		
		logger.debug("Getting cluster by id: {}", clusterId);
		
		return clusters.get(clusterId);
	}

	@Override
	public void create(Cluster cluster) {
		
		logger.debug("Creating cluster with id: {} and description: {}", cluster.getId(), cluster.getDescription());
		
		this.clusters.putIfAbsent(cluster.getId(), cluster);
	}

	@Override
	public void delete(String clusterId) {
		
		logger.debug("Deleting cluster with id: {}", clusterId);
		
		this.clusters.remove(clusterId);
	}

	@Override
	public void update(Cluster cluster) {
		
		logger.debug("Updating cluster with id: {}", cluster.getId());
		
		this.clusters.replace(cluster.getId(), cluster);
	}

	@Override
	public Collection<Cluster> all() {
		
		logger.debug("Getting all clusters.");
		
		return this.clusters.values();
	}

}
