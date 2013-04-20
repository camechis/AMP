package amp.topology.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.eventing.ISerializer;

/**
 * Provides routing info from an HTTP-based endpoint.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class HttpRoutingInfoRetriever implements IRoutingInfoRetriever {

	Logger logger = LoggerFactory.getLogger(HttpRoutingInfoRetriever.class);
	
	HttpClient httpClient;
	
	String urlEndpoint;
	
	ISerializer serializer;
	
	public HttpRoutingInfoRetriever(HttpClientProvider httpClientProvider, String urlEndpoint, ISerializer serializer){
		
		this(httpClientProvider.getClient(), urlEndpoint, serializer);
	}
	
	public HttpRoutingInfoRetriever(HttpClient httpClient, String urlExpression, ISerializer serializer){
		
		this.httpClient = httpClient;
		
		this.urlEndpoint = urlExpression;
		
		this.serializer = serializer;
	}
	
	/**
	 * Retrieve routing info for the supplied routing hints.
	 * @param routingHints Hints used to find the correct routing info.
	 * @return Applicable routing info or null.
	 */
	@Override
	public RoutingInfo retrieveRoutingInfo(Map<String, String> routingHints) {
		
		logger.debug("Getting routing info for hints: {}", routingHints);
		
		RoutingInfo routingInfo = null;
		
		logger.debug("Calling GTS with url: {}", urlEndpoint);
		
		HttpPost httpPost = new HttpPost(urlEndpoint);
		
		 List<NameValuePair> formArgs = new ArrayList<NameValuePair>();
		 
		 for(Entry<String, String> hint : routingHints.entrySet()){
			 
			 formArgs.add(new BasicNameValuePair(hint.getKey(), hint.getValue()));
		 }
		 
         httpPost.setEntity(new UrlEncodedFormEntity(formArgs, Consts.UTF_8));
		
		try {
			
			HttpResponse response = httpClient.execute(httpPost);
			
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
