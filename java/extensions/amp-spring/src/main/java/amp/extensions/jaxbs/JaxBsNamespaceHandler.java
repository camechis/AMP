package amp.extensions.jaxbs;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class JaxBsNamespaceHandler extends NamespaceHandlerSupport {
	
	@Override
	public void init() {
		
		registerBeanDefinitionParser("handler", new MethodBeanDefinitionParser());
		registerBeanDefinitionParser("handlers", new BindingsBeanDefinitionParser());
	}
}