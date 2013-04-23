package amp.topology.core.repo;

import java.util.Collection;

import amp.topology.core.model.ClusterRegistration;


public interface ClusterRegistrationRepository {

	public ClusterRegistration get(String clusterId);
	
	public void create(ClusterRegistration cluster);
	
	public void delete(String clusterId);
	
	public void update(ClusterRegistration cluster);
	
	public Collection<ClusterRegistration> all();
}
