package amp.topology.core.model;

/**
 * Represent a instance of a RabbitMQ Management Console.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ManagementEndpoint extends TopologyModel {
	
	String hostname;
	int port = 15672;
	String username;
	String password;
	String keystore;
	String keystorePassword;
	String truststore;
	String truststorePassword;

	public ManagementEndpoint(){}
	
	public ManagementEndpoint(
		String id, String description,
		String hostname, int port, String username, String password) {
		
		super(id, description);
		
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public ManagementEndpoint(
		String id, String description,
		String hostname, int port, String username, String password, 
		String keystore, String keystorePassword, String truststore, String truststorePassword){
		
		super(id, description);
		
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.keystore = keystore;
		this.keystorePassword = keystorePassword;
		this.truststore = truststore;
		this.truststorePassword = truststorePassword;
		
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKeystore() {
		return keystore;
	}

	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getTruststore() {
		return truststore;
	}

	public void setTruststore(String truststore) {
		this.truststore = truststore;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}
	
}
