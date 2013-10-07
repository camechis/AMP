package amp.utility.http;

import org.apache.http.client.HttpClient;

/**
 * Provide an HTTP Client!  This allows you to 
 * configure the HTTP Client with SSL, Basic Auth,
 * SPNEGO, etc. and pass the configured client
 * to the HttpRoutingInfoRetriever.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public interface HttpClientProvider {

	HttpClient getClient();
}