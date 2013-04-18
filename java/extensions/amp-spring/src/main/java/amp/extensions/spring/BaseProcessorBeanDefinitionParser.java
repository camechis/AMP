package amp.extensions.spring;

import java.util.UUID;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class BaseProcessorBeanDefinitionParser extends BaseBeanDefinitionParser {

	protected abstract Class<?> getInterfaceClass();
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return getInterfaceClass();
	}
	
	@Override
	protected String getAlternateID(Element element,
			AbstractBeanDefinition beanDefinition, ParserContext parserContext) {
		
		return UUID.randomUUID().toString();
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		
		String processorRef = getAttribute("ref", element, STRINGPARSER);
		
		if (processorRef != null){
			
			
		}
	}
}
