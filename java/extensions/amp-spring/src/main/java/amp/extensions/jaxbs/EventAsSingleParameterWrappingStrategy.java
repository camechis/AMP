package amp.extensions.jaxbs;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.eventing.patterns.rpc.IRpcEventBus;

public class EventAsSingleParameterWrappingStrategy implements MethodWrappingStrategy {

	private static Logger logger = LoggerFactory.getLogger(EventAsSingleParameterWrappingStrategy.class);
	
	IRpcEventBus rpcEventBus;
	
	public EventAsSingleParameterWrappingStrategy(IRpcEventBus eventBus){
		
		logger.info("{}", eventBus);
		
		this.rpcEventBus = eventBus;
	}
	
	@Override
	public boolean isAppropriateStrategy(WrappedMethodContext context) {
		
		return !context.isUnwrapped();
	}
	
	@Override
	public void wrapMethod(WrappedMethodContext context) throws Exception {
		
		Object target = context.getRegistration().getTarget();
		Method targetMethod = context.getRegistration().getTargetMethod();
		
		WrappedMethodEventHandler handler = null;
		
		if (context.hasSendableReturnType() && context.getRegistration().shouldRespondToEventIfApplicable()){
		
			handler = new RespondingWrappedMethodEventHandler(this.rpcEventBus, target, targetMethod);
		} 
		else {
			
			handler = new WrappedMethodEventHandler(target, targetMethod);
		}
		
		this.rpcEventBus.subscribe(handler);
	}
}