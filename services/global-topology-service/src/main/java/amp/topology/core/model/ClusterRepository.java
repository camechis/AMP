package amp.topology.core.model;

import java.util.Collection;


public interface ClusterRepository {

	public Cluster get(String clusterId);
	
	public void create(Cluster cluster);
	
	public void delete(String clusterId);
	
	public void update(Cluster cluster);
	
	public Collection<Cluster> all();
}
