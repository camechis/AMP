package amp.topology.core.model;

import java.util.ArrayList;
import java.util.Collection;


public class ClusterRegistration extends TopologyModel {

	protected ArrayList<ManagementEndpoint> managementEndpoints = new ArrayList<ManagementEndpoint>();
	
	public ClusterRegistration() { super(); }

	public ClusterRegistration(String id, String description) {
		
		super(id, description);
	}
	
	public ClusterRegistration(String id, String description, Collection<ManagementEndpoint> managementEndpoints) {
		
		super(id, description);
		
		setManagementEndpoints(managementEndpoints);
	}

	public Collection<ManagementEndpoint> getManagementEndpoints() {
		return managementEndpoints;
	}

	public void setManagementEndpoints(
			Collection<ManagementEndpoint> managementEndpoints) {
		
		this.managementEndpoints.addAll(managementEndpoints);
	}
}