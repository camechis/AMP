package amp.extensions.jaxbs;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.eventing.IEventHandler;

public class WrappedMethodEventHandler implements IEventHandler<Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(WrappedMethodEventHandler.class);
	
	Object target;
	Method targetMethod;
	String methodName;
	
	public WrappedMethodEventHandler(Object target, Method targetMethod){
		
		this.target = target;
		this.targetMethod = targetMethod;
		this.methodName = String.format("%s.%s()", 
			target.getClass().getCanonicalName(), targetMethod.getName());
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getEventType() {
		
		return (Class) targetMethod.getGenericParameterTypes()[0];
	}

	@Override
	public Object handle(Object event, Map<String, String> context) {
		
		logger.debug("Handling event on method [{}] with context: {}.", this.methodName, context);
		
		Object result = null;
		
		try {
			
			result = targetMethod.invoke(target, event);
			
		} catch (Exception e) {
				
			logger.error("Error calling target method: {}", e);
		}
		
		return result;
	}

	@Override
	public Object handleFailed(Envelope envelope, Exception ex) {
		
		return null;
	}
}