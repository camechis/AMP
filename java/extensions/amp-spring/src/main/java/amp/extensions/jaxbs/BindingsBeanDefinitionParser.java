package amp.extensions.jaxbs;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class BindingsBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return WrappedMethodRegistry.class;
	}
	
	@Override
	protected String resolveId(Element element, AbstractBeanDefinition beanDefinition,
			ParserContext parserContext) throws BeanDefinitionStoreException {
		
		String id = element.getAttribute("id");
		
		if (!StringUtils.hasText(id)){
			
			id = String.format("bindings-%s", UUID.randomUUID());
		}
		
		return id;
	}
	
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		
		String rpcEventBusRef = element.getAttribute("rpcEventBus-ref");
		
		if (!StringUtils.hasText(rpcEventBusRef)){
			
			rpcEventBusRef = "rpcEventBus";
		}
		
		List<Element> methods = DomUtils.getChildElementsByTagName(element, "handler");
		
		if (methods != null && !methods.isEmpty()){
			
			ManagedList<BeanDefinition> methodList = new ManagedList<BeanDefinition>();
			
			for (Element method : methods){
				
				BeanDefinition definition = parserContext.getDelegate().parseCustomElement(method);
				
				methodList.add(definition);
			}
			
			bean.addPropertyValue("registrations", methodList);
		}
		
		String strategies = element.getAttribute("strategies");
		if (StringUtils.hasText(strategies)){
			
			bean.addPropertyReference("strategies", strategies);
			
		} else {
			
			System.out.println("No strategies!");
			
			ManagedList<BeanDefinition> strategyList = new ManagedList<BeanDefinition>();
			
			BeanDefinitionBuilder eventAsSingleParamWrappingStrategyBuilder = 
				BeanDefinitionBuilder.rootBeanDefinition(EventAsSingleParameterWrappingStrategy.class);
			
			eventAsSingleParamWrappingStrategyBuilder.addConstructorArgReference(rpcEventBusRef);
			
			strategyList.add(eventAsSingleParamWrappingStrategyBuilder.getBeanDefinition());
			
			bean.addPropertyValue("strategies", strategyList);
		}
		
		String injects = element.getAttribute("injects");
		
		if (StringUtils.hasText(injects)){
			
			bean.addPropertyReference("injects", injects);
		}
		
		bean.setInitMethodName("initialize");
	}
}
