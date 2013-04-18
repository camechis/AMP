package amp.extensions.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class AmpNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		
		registerBeanDefinitionParser("gts", new GtsBeanDefinitionParser());
		registerBeanDefinitionParser("fallback", new FallbackBeanDefinitionParser());
		registerBeanDefinitionParser("transport-rabbitmq", new RabbitTransportBeanDefinitionParser());
	}
	
}