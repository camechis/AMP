package amp.bus.rabbit;


import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultSaslConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.BaseRoute;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;


public class CertificateChannelFactory extends BaseChannelFactory {

    protected Logger log;
	protected String password;
    protected String pathToClientCert;
    protected String pathToRemoteCertStore;
    protected String passwordToRemoteCertStore = null;

    public CertificateChannelFactory(String pathToClientCertificate, String password, String pathToRemoteCertStore) {

        log = LoggerFactory.getLogger(this.getClass());
        pathToClientCert = pathToClientCertificate;
        this.password = password;
        this.pathToRemoteCertStore = pathToRemoteCertStore;
    }
    
    public CertificateChannelFactory(
    		String pathToClientCertificate, String password, 
    		String pathToRemoteCertStore, String passwordToRemoteCertStore) {

        this(pathToClientCertificate, password, pathToRemoteCertStore);
        this.passwordToRemoteCertStore = passwordToRemoteCertStore;
    }
	
	Connection createConnection(Broker broker, BaseRoute route) throws Exception {

        log.debug("Getting connection for broker: {}", broker);

		char[] keyPassphrase = password.toCharArray();

        KeyStore clientCertStore = KeyStore.getInstance("PKCS12");
        clientCertStore.load(new FileInputStream(pathToClientCert), keyPassphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientCertStore, keyPassphrase);

        KeyStore remoteCertStore = KeyStore.getInstance("JKS");
        remoteCertStore.load(new FileInputStream(pathToRemoteCertStore), 
        		(this.passwordToRemoteCertStore != null)? 
        				this.passwordToRemoteCertStore.toCharArray() : null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(remoteCertStore);

        SSLContext c = SSLContext.getInstance("SSLv3");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(broker.getHostname());
        factory.setPort(broker.getPort());
        factory.setVirtualHost(route.getExchange().getVirtualHost());
        factory.setSaslConfig(DefaultSaslConfig.EXTERNAL);
        factory.useSslProtocol(c);
        //factory.setRequestedHeartbeat(HEARTBEAT_INTERVAL);
        
        return factory.newConnection();
	}

	@Override
	public ConnectionContext getConnection(Broker broker, ProducingRoute route)
			throws Exception {
		
		Connection connection = createConnection(broker, route);
		
		return new ConnectionContext(broker, route, connection);
	}

	@Override
	public ConnectionContext getConnection(Broker broker, ConsumingRoute route)
			throws Exception {
		
		Connection connection = createConnection(broker, route);
		
		return new ConnectionContext(broker, route, connection);
	}

}