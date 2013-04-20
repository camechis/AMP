package amp.topology.core.repo;

import java.util.Collection;

import amp.topology.core.model.Cluster;


public interface ClusterRepository {

	public Cluster get(String clusterId);
	
	public void create(Cluster cluster);
	
	public void delete(String clusterId);
	
	public void update(Cluster cluster);
	
	public Collection<Cluster> all();
}
