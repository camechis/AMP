package amp.extensions.jaxbs;

import java.util.UUID;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class MethodBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	
	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return MethodRegistration.class;
	}
	
	@Override
	protected String resolveId(Element element, AbstractBeanDefinition beanDefinition,
			ParserContext parserContext) throws BeanDefinitionStoreException {
		
		String id = element.getAttribute("id");
		String targetMethodName = element.getAttribute("method");
		
		if (!StringUtils.hasText(id)){
			
			id = String.format("method-%s-%s", targetMethodName, UUID.randomUUID());
		}
		
		return id;
	}
	
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		
		String target = element.getAttribute("target-ref");
		bean.addPropertyReference("target", target);
		
		String method = element.getAttribute("method");
		bean.addPropertyValue("targetMethodName", method);
		
		String strRespondToEvent = element.getAttribute("respond-to-event");
		
		if (strRespondToEvent != null && StringUtils.hasText(strRespondToEvent)){
			
			boolean respondToEvent = Boolean.parseBoolean(strRespondToEvent);
			
			bean.addPropertyValue("respondToEventIfApplicable", respondToEvent);
		}
	}
}
