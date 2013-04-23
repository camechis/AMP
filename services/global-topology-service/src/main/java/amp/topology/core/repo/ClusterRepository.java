package amp.topology.core.repo;

import java.util.Collection;

import amp.topology.core.model.Cluster;
import amp.topology.core.model.ClusterDoesntExistException;

public interface ClusterRepository {

	Collection<Cluster> all();
	
	Cluster get(String cluster) throws ClusterDoesntExistException;
	
}
