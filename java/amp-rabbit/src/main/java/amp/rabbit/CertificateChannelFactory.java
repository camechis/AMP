package amp.rabbit;


import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.topology.Exchange;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultSaslConfig;


public class CertificateChannelFactory extends BaseChannelFactory {

    protected Logger log;
	protected String password;
    protected String pathToClientCert;
    protected String pathToRemoteCertStore;

    public CertificateChannelFactory(String pathToClientCertificate, String password, String pathToRemoteCertStore) {

        log = LoggerFactory.getLogger(this.getClass());
        pathToClientCert = pathToClientCertificate;
        this.password = password;
        this.pathToRemoteCertStore = pathToRemoteCertStore;
    }
	
	@Override
	public void configureConnectionFactory(ConnectionFactory factory, Exchange exchange) throws Exception {

        log.debug("Getting connection for exchange: {}", exchange.toString());

		char[] keyPassphrase = password.toCharArray();

        KeyStore clientCertStore = KeyStore.getInstance("PKCS12");
        clientCertStore.load(new FileInputStream(pathToClientCert), keyPassphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientCertStore, keyPassphrase);

        KeyStore remoteCertStore = KeyStore.getInstance("JKS");
        remoteCertStore.load(new FileInputStream(pathToRemoteCertStore), null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(remoteCertStore);

        SSLContext c = SSLContext.getInstance("SSLv3");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    	super.configureConnectionFactory(factory, exchange);
        factory.setSaslConfig(DefaultSaslConfig.EXTERNAL);
        factory.useSslProtocol(c);
	}

}