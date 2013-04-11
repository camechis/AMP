package amp.topology.core.model;

import java.util.ArrayList;
import java.util.Collection;


public class Cluster extends TopologyModel {

	protected ArrayList<ManagementEndpoint> managementEndpoints = new ArrayList<ManagementEndpoint>();
	
	public Cluster() { super(); }

	public Cluster(String id, String description) {
		
		super(id, description);
	}
	
	public Cluster(String id, String description, Collection<ManagementEndpoint> managementEndpoints) {
		
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