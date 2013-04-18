package amp.extensions.jaxbs;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrappedMethodContext {
	
	public static List<Class<?>> BLACKLISTED_RETURN_TYPES = new ArrayList<Class<?>>();
	
	static {
		BLACKLISTED_RETURN_TYPES.add(Date.class);
		BLACKLISTED_RETURN_TYPES.add(String.class);
		BLACKLISTED_RETURN_TYPES.add(File.class);
		BLACKLISTED_RETURN_TYPES.add(Class.class);
		BLACKLISTED_RETURN_TYPES.add(Thread.class);
	}
	
	MethodRegistration methodRegistration;
	HashMap<Class<?>, Object> registeredInjects = new HashMap<Class<?>, Object>();
	
	// Calculated properties
	int paramCount = 0;
	HashMap<Class<?>, Object> requiredInjects = new HashMap<Class<?>, Object>();
	Class<?> methodReturnType = null;
	
	public WrappedMethodContext(MethodRegistration registration, Map<Class<?>, Object> injects){
		
		this.methodRegistration = registration;
		
		if (injects != null){
			this.registeredInjects.putAll(injects);
		}
		
		initialize();
	}
	
	void initialize(){
		
		for(Type t : this.methodRegistration.getTargetMethod().getGenericParameterTypes()){
			
			if (!hasTypeAsInject(t)){
				
				paramCount++;
			}
			else {
				
				Class<?> injectableType = (Class<?>)t; 				
				
				Object injectable = this.registeredInjects.get(injectableType);
				
				this.requiredInjects.put(injectableType, injectable);
			}
		}
		
		if (paramCount == 0){
			
			throw new RuntimeException("Cannot wrap a method with no valid parameters to turn into subscriptions.");
		}
		
		Class<?> returnType = (Class<?>)this.methodRegistration.getTargetMethod().getGenericReturnType();
		
		if (!isBlackListed(returnType)){
			
			this.methodReturnType = returnType;
		}
	}

	public MethodRegistration getRegistration(){
		
		return this.methodRegistration;
	}
	
	public boolean isUnwrapped(){
		
		return this.paramCount > 1;
	}
	
	public boolean hasSendableReturnType(){
		
		return this.methodReturnType != null;
	}
	
	public Class<?> getSendableReturnType(){
		
		return this.methodReturnType;
	}
		
	boolean hasClassAsInject(Class<?> classToCheck){
		
		return this.registeredInjects.containsKey(classToCheck);
	}
	
	boolean hasTypeAsInject(Type typeToCheck){
		
		return hasClassAsInject((Class<?>)typeToCheck);
	}
	
	boolean isBlackListed(Class<?> classToCheck){
		
		return (classToCheck.isPrimitive() || BLACKLISTED_RETURN_TYPES.contains(classToCheck));
	}
}