package amp.topology.core.factory.rmq;

public class RmqCompositeKey {
	
	protected String cluster;
	protected String vhost;
	protected String name;
	protected String type;
	
	public RmqCompositeKey(String cluster, String vhost, String name, String type) {

		this.cluster = cluster;
		this.vhost = vhost;
		this.name = name;
		this.type = type;
	}

	public String getCluster() {
		return cluster;
	}

	public String getVhost() {
		return vhost;
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
}