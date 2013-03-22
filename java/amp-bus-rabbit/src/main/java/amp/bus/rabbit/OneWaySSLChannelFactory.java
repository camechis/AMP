package amp.bus.rabbit;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import amp.bus.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.NullTrustManager;

public class OneWaySSLChannelFactory extends BaseChannelFactory {

	private static final String DEFAULT_SSL_PROTOCOL = "TLSv1";
	
	protected String password;
    protected String username;

    public OneWaySSLChannelFactory(String username, String password) {
		super();
		
		this.username = username;
		this.password = password;
   }

	@Override
	public Connection getConnection(Exchange exchange) throws IOException {
		
		ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setVirtualHost(exchange.getVirtualHost());
        //factory.setRequestedHeartbeat(HEARTBEAT_INTERVAL);
        
        try {
			factory.setSocketFactory(getPermissiveSSLContext().getSocketFactory());
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
        
        return factory.newConnection();
	}

	private SSLContext getPermissiveSSLContext() throws GeneralSecurityException{
		SSLContext instance = SSLContext.getInstance(DEFAULT_SSL_PROTOCOL);
		instance.init(null, new TrustManager[]{new NullTrustManager()}, null);
		return instance;
	}
}