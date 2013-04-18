package amp.extensions.jaxbs;

import java.lang.reflect.Method;
import java.util.Map;

import cmf.eventing.patterns.rpc.IRpcEventBus;

public class RespondingWrappedMethodEventHandler extends WrappedMethodEventHandler {

	IRpcEventBus rpcEventBus;
	
	public RespondingWrappedMethodEventHandler(
			IRpcEventBus rpcEventBus, Object target, Method targetMethod) {
		
		super(target, targetMethod);
		
		this.rpcEventBus = rpcEventBus;
	}

	@Override
	public Object handle(Object event, Map<String, String> context) {
		
		Object result = super.handle(event, context);
		
		if (result != null){
			
			rpcEventBus.respondTo(context, result);
		}
			
		return result;
	}
}