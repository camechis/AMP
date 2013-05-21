package amp.topology.client;

import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.topology.RoutingInfo;
import amp.eventing.ISerializer;

/**
 * Provides routing info from an HTTP-based endpoint.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class HttpRoutingInfoRetriever implements IRoutingInfoRetriever {

	Logger logger = LoggerFactory.getLogger(HttpRoutingInfoRetriever.class);
	
	HttpClient httpClient;
	
	String urlExpression;
	
	ISerializer serializer;
	
	public HttpRoutingInfoRetriever(HttpClientProvider httpClientProvider, String urlExpression, ISerializer serializer){
		
		this(httpClientProvider.getClient(), urlExpression, serializer);
	}
	
	public HttpRoutingInfoRetriever(HttpClient httpClient, String urlExpression, ISerializer serializer){
		
		this.httpClient = httpClient;
		
		this.urlExpression = urlExpression;
		
		this.serializer = serializer;
	}
	
	/**
	 * Retrieve routing info for the supplied topic.
	 * @param topic Topic to find routing info for.
	 * @return Applicable routing info or null.
	 */
	@Override
	public RoutingInfo retrieveRoutingInfo(String topic) {
		
		logger.debug("Getting routing info for topic: {}", topic);
		
		RoutingInfo routingInfo = null;
		
		String url = String.format(this.urlExpression, topic);
		
		logger.debug("Calling GTS with url: {}", url);
		
		HttpGet httpGet = new HttpGet(url);
		
		try {
			
			HttpResponse response = httpClient.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			
			String content = EntityUtils.toString(entity);
			
			logger.debug("Received the following content from GTS: {}", content);
			
			routingInfo = this.serializer.stringDeserialize(content, RoutingInfo.class);
			
		} catch (Exception e){
			
			logger.error("Failed to retrieve routing info: {}", e);
		}
		
		return routingInfo;
	}

}
