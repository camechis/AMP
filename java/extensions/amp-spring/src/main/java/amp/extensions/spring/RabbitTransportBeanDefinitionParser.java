package amp.extensions.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import amp.bus.rabbit.BasicChannelFactory;
import amp.bus.rabbit.CertificateChannelFactory;
import amp.bus.rabbit.IRabbitChannelFactory;
import amp.bus.rabbit.RabbitTransportProvider;

public class RabbitTransportBeanDefinitionParser extends BaseBeanDefinitionParser {
	
	@Override
	protected String getAlternateID(
		Element element, AbstractBeanDefinition beanDefinition, ParserContext parserContext) {
		
		return "transportProvider";
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return RabbitTransportProvider.class;
	}
	
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		
		BeanDefinition gts = getBeanDefinitionFromElement(element, "gts", parserContext);
		
		if (gts != null){
			
			bean.addConstructorArgValue(gts);
		}
		
		String topologyServiceRef = getAttribute("topology-service-ref", element, STRINGPARSER);
		
		if (topologyServiceRef != null){
			
			bean.addConstructorArgReference(topologyServiceRef);
		}
		
		String channelFactoryRef = getAttribute("channel-factory-ref", element, STRINGPARSER);
		
		if (channelFactoryRef != null) {
		
			bean.addConstructorArgReference(channelFactoryRef);
		}
		else {
			
			IRabbitChannelFactory channelFactory = null;
			
			String username = getAttribute("username", element, STRINGPARSER);
			String password = getAttribute("password", element, STRINGPARSER);
			
			String keystore = getAttribute("keystore", element, STRINGPARSER);
			String keystorePassword = getAttribute("keystorePassword", element, STRINGPARSER);
			String truststore = getAttribute("truststore", element, STRINGPARSER);
			
			// TODO: CertificateChannelFactory should support truststore password.
			@SuppressWarnings("unused")
			String truststorePassword = getAttribute("truststorePassword", element, STRINGPARSER);
			
			if (username != null && password != null){
				
				channelFactory = new BasicChannelFactory(username, password);
			}
			else {
				
				channelFactory = new CertificateChannelFactory(keystore, keystorePassword, truststore);
			}
			
			bean.addConstructorArgValue(channelFactory);
		}
	}

}
