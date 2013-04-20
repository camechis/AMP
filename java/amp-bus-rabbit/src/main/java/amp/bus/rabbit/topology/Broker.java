package amp.bus.rabbit.topology;

/**
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class Broker {

	/**
	 * This is used primarily to coach the ChannelFactory
	 * to the credentials that should be used for this broker.
	 */
	protected String clusterId;
	protected String hostname;
	protected int port = 5672;
	protected boolean isSslEnabled = false;
	
	public Broker(){}
	
	public Broker(String clusterId, String hostname, int port, boolean isSslEnabled) {
		
		this.clusterId = clusterId;
		this.hostname = hostname;
		this.port = port;
		this.isSslEnabled = isSslEnabled;
	}

	public String getClusterId() {
		return clusterId;
	}
	
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isSslEnabled() {
		return isSslEnabled;
	}
	
	public void setSslEnabled(boolean isSslEnabled) {
		this.isSslEnabled = isSslEnabled;
	}
	
	@Override
	public String toString() {
		return "Broker [clusterId=" + clusterId + ", hostname=" + hostname
				+ ", port=" + port + ", isSslEnabled=" + isSslEnabled + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clusterId == null) ? 0 : clusterId.hashCode());
		result = prime * result
				+ ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + (isSslEnabled ? 1231 : 1237);
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Broker other = (Broker) obj;
		if (clusterId == null) {
			if (other.clusterId != null)
				return false;
		} else if (!clusterId.equals(other.clusterId))
			return false;
		if (hostname == null) {
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (isSslEnabled != other.isSslEnabled)
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public static BrokerBuilder builder(){
		
		return new BrokerBuilder();
	}
	
	/**
	 * Simplifies construction of a Broker object.
	 * 
	 * @author Richard Clayton (Berico Technologies)
	 */
	public static class BrokerBuilder {
		
		Broker broker = new Broker();
		
		public BrokerBuilder cluster(String clusterId){
			
			broker.setClusterId(clusterId);
			
			return this;
		}
		
		public BrokerBuilder host(String host){
					
					broker.setHostname(host);
					
					return this;
				}
		
		public BrokerBuilder port(int port){
			
			broker.setPort(port);
			
			return this;
		}
		
		public BrokerBuilder useSsl(boolean useSsl){
			
			broker.setSslEnabled(useSsl);
			
			return this;
		}
		
		public Broker build(){
			
			return this.broker;
		}
	}
}