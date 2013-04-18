package amp.extensions.spring;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import amp.topology.client.DefaultApplicationExchangeProvider;

public class FallbackBeanDefinitionParser extends BaseBeanDefinitionParser  {

	
	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return DefaultApplicationExchangeProvider.class;
	}
	
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		
		addProperty("exchange", "exchangeName", STRINGPARSER, element, bean);
		addProperty("host", "hostname", STRINGPARSER, element, bean);
		addProperty("vhost", "vhost", STRINGPARSER, element, bean);
		addProperty("port", "port", INTPARSER, element, bean);
		addProperty("clientName", "clientName", STRINGPARSER, element, bean);
		addProperty("exchange-type", "exchangeType", STRINGPARSER, element, bean);
		addProperty("queue", "queueName", STRINGPARSER, element, bean);
		addProperty("durable", "durable", BOOLEANPARSER, element, bean);
		addProperty("auto-delete", "autoDelete", BOOLEANPARSER, element, bean);
	}
	
	@Override
	protected String getAlternateID(
			Element element, AbstractBeanDefinition beanDefinition, ParserContext parserContext) {
		
		return "fallbackProvider";
	}
}
