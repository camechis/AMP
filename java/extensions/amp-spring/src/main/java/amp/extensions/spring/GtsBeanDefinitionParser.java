package amp.extensions.spring;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import amp.topology.client.BasicAuthHttpClientProvider;
import amp.topology.client.DefaultApplicationExchangeProvider;
import amp.topology.client.GlobalTopologyService;
import amp.topology.client.HttpClientProvider;
import amp.topology.client.HttpRoutingInfoRetriever;
import amp.topology.client.JsonRoutingInfoSerializer;
import amp.topology.client.SslHttpClientProvider;

public class GtsBeanDefinitionParser extends BaseBeanDefinitionParser {
	
	public static long DEFAULT_CACHE_EXPIRY = 1000 * 60 * 10;
	
	@Override
	protected String getAlternateID(Element element,
			AbstractBeanDefinition beanDefinition, ParserContext parserContext) {
		
		return "globalTopologyService";
	}
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return GlobalTopologyService.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		
		URI uri = null;
		long expiryTimeMillis = DEFAULT_CACHE_EXPIRY;
		
		String url = element.getAttribute("url");
		String username = element.getAttribute("username");
		String password = element.getAttribute("password");
		String keystore = element.getAttribute("keystore");
		String keystorePassword = element.getAttribute("keystorePassword");
		String truststore = element.getAttribute("truststore");
		String truststorePassword = element.getAttribute("truststorePassword");
		
		try {
			
			String partialUrl = url.substring(0, url.lastIndexOf('/'));
			
			uri = new URI(partialUrl);
			
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
			
			throw new RuntimeException("'gts:url' is not a valid URI.");
		}
		
		JsonRoutingInfoSerializer serializer = new JsonRoutingInfoSerializer();
		
		HttpClientProvider provider = null;
		
		if (isValid(username) && isValid(password)){
			
			provider = 
				new BasicAuthHttpClientProvider(uri.getHost(), uri.getPort(), username, password);
		}
		else if (isValid(keystore) && isValid(keystorePassword)){
			
			provider = 
				new SslHttpClientProvider(
					keystore, keystorePassword, 
					// Truststore must be null, not an empty string
					isValid(truststore)? truststore : null, 
					// Why not?
					isValid(truststorePassword)? truststorePassword : null, 
					uri.getPort());
		}
		
		HttpRoutingInfoRetriever retriever = new HttpRoutingInfoRetriever(provider, url, serializer);
		
		bean.addConstructorArgValue(retriever);
		
		String strExpiryTimeMillis = element.getAttribute("expiryTimeMillis");
		
		if (strExpiryTimeMillis != null && StringUtils.hasText(strExpiryTimeMillis)){
			
			try {
				
				expiryTimeMillis = Long.parseLong(strExpiryTimeMillis);
				
			} catch (NumberFormatException nfe){
			
				logger.warn("'gts:expiryTimeMillis' is not a valid long.");
			}
		}
		
		bean.addConstructorArgValue(expiryTimeMillis);
		
		Element fallbackElement = DomUtils.getChildElementByTagName(element, "fallback");
		
		String fallbackRef = element.getAttribute("fallback-ref");
		
		// Nested amp:fallback declaration
		if (fallbackElement != null){
			
			BeanDefinition definition = parserContext.getDelegate().parseCustomElement(fallbackElement);
			
			bean.addConstructorArgValue(definition);
		}
		// Using fallback-ref as an attribute on amp:gts
		else if (isValid(fallbackRef)){
			
			addConstructorArgRef("fallback-ref", element, bean);
		}
		// Instantiate and use the default fallback provider.
		else {
			
			DefaultApplicationExchangeProvider fallback = new DefaultApplicationExchangeProvider();
			
			fallback.setHostname(uri.getHost());
			fallback.setDurable(true);
			
			bean.addConstructorArgValue(fallback);
		}
	}

	
	
	
}
