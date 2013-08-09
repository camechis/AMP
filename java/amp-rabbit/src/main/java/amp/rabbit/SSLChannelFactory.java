package amp.rabbit;

import amp.rabbit.topology.Exchange;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultSaslConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SSLChannelFactory extends BaseChannelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SSLChannelFactory.class);

    private String _pathToRabbitTrustStore;
    private String _keystorePassword;
    private String _username;
    private String _password;


    public void setUsername(String value) { _username = value; }
    public void setPassword(String value) { _password = value; }


    public SSLChannelFactory(String pathToRabbitTrustStore, String keystorePassword) {
        _pathToRabbitTrustStore = pathToRabbitTrustStore;
        _keystorePassword = keystorePassword;
    }

    public SSLChannelFactory(
            String username,
            String password,
            String pathToRabbitTrustStore,
            String keystorePassword) {

        _username = username;
        _password = password;
        _pathToRabbitTrustStore = pathToRabbitTrustStore;
        _keystorePassword = keystorePassword;
    }


    @Override
    public Connection getConnection(Exchange exchange) throws Exception {

        LOG.debug("Getting connection for exchange: {}", exchange.toString());

        // apparently, a string isn't good enough
        char[] keyPassphrase = _keystorePassword.toCharArray();

        // load the java key store
        KeyStore remoteCertStore = KeyStore.getInstance("JKS");
        remoteCertStore.load(new FileInputStream(_pathToRabbitTrustStore), keyPassphrase);

        // use it to build the trust manager
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(remoteCertStore);

        // initialize a context for SSL-protected (not mutual auth) connection
        SSLContext c = SSLContext.getInstance("SSLv3");
        c.init(null, tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(_username);
        factory.setPassword(_password);
        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setVirtualHost(exchange.getVirtualHost());
        factory.setSaslConfig(DefaultSaslConfig.EXTERNAL);
        factory.useSslProtocol(c);

        return factory.newConnection();
    }
}