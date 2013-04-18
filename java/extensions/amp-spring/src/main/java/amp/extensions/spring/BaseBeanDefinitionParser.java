package amp.extensions.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public abstract class BaseBeanDefinitionParser extends AbstractSingleBeanDefinitionParser  {

	protected Logger logger;
	
	public BaseBeanDefinitionParser(){
		
		this.logger = LoggerFactory.getLogger(this.getClass());
	}
	
	protected abstract String getAlternateID(
			Element element, 
			AbstractBeanDefinition beanDefinition,
			ParserContext parserContext);
	
	@Override
	protected String resolveId(
			Element element, AbstractBeanDefinition beanDefinition,
			ParserContext parserContext) throws BeanDefinitionStoreException {
		
		String id = element.getAttribute("id");
		
		if (!StringUtils.hasText(id)){
			
			id = this.getAlternateID(element, beanDefinition, parserContext);
		}
		
		return id;
	}
	
	protected void addProperty(
			String xmlAttributeName, String propertyName, 
			StringParser<?> parser, Element element, BeanDefinitionBuilder bean){
		
		String propertyAsString = element.getAttribute(xmlAttributeName);
		
		if (isValid(propertyAsString)){
			
			Object propertyValue = parser.parse(propertyAsString);
			
			bean.addPropertyValue(propertyName, propertyValue);
		}
	}
	
	protected void addPropertyRef(
			String xmlAttributeName, String propertyName, 
			Element element, BeanDefinitionBuilder bean){
		
		String propertyRefAsString = element.getAttribute(xmlAttributeName);
		
		if (isValid(propertyRefAsString)){
			
			bean.addPropertyReference(propertyName, propertyRefAsString);
		}
	}
	
	protected void addConstructorArg(
			String xmlAttributeName, StringParser<?> parser, Element element, BeanDefinitionBuilder bean){
		
		String constructorArgAsString = element.getAttribute(xmlAttributeName);
		
		if (isValid(constructorArgAsString)){
			
			Object constructorArgValue = parser.parse(constructorArgAsString);
			
			bean.addConstructorArgValue(constructorArgValue);
		}
	}
	
	protected void addConstructorArgRef(
			String xmlAttributeName, Element element, BeanDefinitionBuilder bean){
		
		String constructorArgRefAsString = element.getAttribute(xmlAttributeName);
		
		if (isValid(constructorArgRefAsString)){
			
			bean.addConstructorArgReference(constructorArgRefAsString);
		}
	}
	
	protected boolean isValid(String value){
		
		return value != null && StringUtils.hasText(value);
	}
	
	protected <T> T getAttribute(String xmlAttribute, Element element, StringParser<T> parser){
		
		String property = element.getAttribute(xmlAttribute);
		
		if (isValid(property)){
		
			return parser.parse(property);
		}
		
		return null;
	}
	
	protected BeanDefinition getBeanDefinitionFromElement(
		Element element, String targetElementName, ParserContext parserContext){
		
		Element target = DomUtils.getChildElementByTagName(element, targetElementName);
		
		if (target != null){
		
			return parserContext.getDelegate().parseCustomElement(target);
		}
		
		return null;
	}
	
	
	public interface StringParser<T> {
		
		T parse(String value);
	}
	
	public final static StringParser<Integer> INTPARSER = new StringParser<Integer>(){
		
		@Override
		public Integer parse(String value) { return Integer.parseInt(value); }
	};
	
	public final static StringParser<Boolean> BOOLEANPARSER = new StringParser<Boolean>(){
		
		@Override
		public Boolean parse(String value) { return Boolean.parseBoolean(value); }
	};
	
	public final static StringParser<Long> LONGPARSER = new StringParser<Long>(){
		
		@Override
		public Long parse(String value) { return Long.parseLong(value); }
	};
	
	public final static StringParser<String> STRINGPARSER = new StringParser<String>(){
		
		@Override
		public String parse(String value) { return value; }
	};
}
