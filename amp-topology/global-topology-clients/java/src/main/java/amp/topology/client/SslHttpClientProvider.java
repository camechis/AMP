package amp.topology.client;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides an HttpClient configured for SSL Mutual Auth.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class SslHttpClientProvider implements HttpClientProvider {

	private static final Logger logger = LoggerFactory.getLogger(SslHttpClientProvider.class);
	
	int port;
	
	String keystore;
	
	String password;
	
	public SslHttpClientProvider(String keystore, String keystorePassword, int port){
		
		this.keystore = keystore;
		this.password = keystorePassword;
		this.port = port;
	}
	
	@Override
	public HttpClient getClient() {
		
		return this.createClient();
	}

	/**
	 * Register a Trust Store with the HttpClient.
	 * @param httpClient HttpClient to register the trust store.
	 * @param port Port of the server in which the connection will be made.
	 * @param keystorePath Path on disk to the keystore.
	 * @param keystorePassword Password of the keystore.
	 * @throws Exception If the file can't be pulled from the FS, an exception is thrown.
	 */
	public HttpClient createClient() {
		
			logger.debug("Creating HttpClient.");
		
			FileInputStream instream = null;
			SSLSocketFactory socketFactory = null;
			
			try {
			
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				
				instream  = new FileInputStream(new File(this.keystore));
				
				trustStore.load(instream, this.password.toCharArray());
				
				socketFactory = new SSLSocketFactory(trustStore, this.password, trustStore);
				
			} catch (Exception e){  
				
				logger.error("Could not instantiate an SSLSocketFactory: {}", e);
			}  
			finally {
			
				try { instream.close(); } catch (Exception ignore) {}
			}
			
			Scheme scheme = new Scheme("https", this.port, socketFactory);
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
			
			return httpClient;
		}
}
