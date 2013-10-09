package amp.extensions.commons.builder;

import amp.rabbit.connection.BasicConnectionFactory;
import amp.rabbit.connection.CertificateConnectionFactory;

/**
 * Build up a Channel Factory.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ConnectionFactoryBuilder extends FluentExtension {
	
	TransportBuilder transportBuilder;
	
	/**
	 * Initialize the builder with a reference to the parent fluent interface.
	 * @param parent Parent builder.
	 */
	public ConnectionFactoryBuilder(BusBuilder parent, TransportBuilder transportBuilder) {
		
		super(parent);
		
		this.transportBuilder = transportBuilder;
	}

	/**
	 * Connect to brokers using basic authentication.
	 * @param username Username.
	 * @param password Password.
	 * @return Parent fluent.
	 */
	public TransportBuilder basicAuth(String username, String password){
		
		BasicConnectionFactory channelFactory = new BasicConnectionFactory(username, password);
		
		this.transportBuilder.setConnectionFactory(channelFactory);
		
		return this.transportBuilder;
	}
	
	/**
	 * Connect to brokers using SSL (certificate authentication).
	 * @param keystore Path to Client Certificate.
	 * @param certPassword Password for Certificate.
	 * @param pathToTrustStore Path to Trust Store.
	 * @return Parent fluent.
	 */
	public TransportBuilder sslAuth(
		String pathToClientCert, String certPassword, String pathToTrustStore){
		
		CertificateConnectionFactory channelFactory =
			new CertificateConnectionFactory(pathToClientCert, certPassword, pathToTrustStore);
		
		this.transportBuilder.setConnectionFactory(channelFactory);
		
		return this.transportBuilder;
	}

	/**
	 * And go back to the primary fluent interface.
	 * @return original interface.
	 */
	@Override
	public BusBuilder and() {
		
		return this.transportBuilder.and();
	}
	
	
}
