package amp.topology.core.model;

public class ClusterDoesntExistException extends Exception {
	
	private static final long serialVersionUID = 7872474217331263607L;

	public ClusterDoesntExistException(String clusterId){
		
		super(String.format("Cluster '%s' does not exist.", clusterId));
	}
	
}
