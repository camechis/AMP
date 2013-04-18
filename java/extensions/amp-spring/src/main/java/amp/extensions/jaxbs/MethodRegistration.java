package amp.extensions.jaxbs;

import java.lang.reflect.Method;

public class MethodRegistration {
	
	Object target;
	Method targetMethod = null;
	String targetMethodName = null;
	boolean respondToEventIfApplicable = true;
	String nameOfEventToConsume = null;
	String nameOfEventToProduce = null;
	
	public MethodRegistration(){}
	
	public MethodRegistration(Object target, Method targetMethod,
			boolean respondToEventIfApplicable, String nameOfEventToConsume,
			String nameOfEventToProduce) {
		
		this.target = target;
		this.targetMethod = targetMethod;
		this.respondToEventIfApplicable = respondToEventIfApplicable;
		this.nameOfEventToConsume = nameOfEventToConsume;
		this.nameOfEventToProduce = nameOfEventToProduce;
	}

	public Object getTarget() {
		return target;
	}
	
	public void setTarget(Object target) {
		this.target = target;
	}
	
	public Method getTargetMethod() {
	
		if (targetMethod == null && this.targetMethodName != null){
			
			this.targetMethod = getMethodByName(this.target, this.targetMethodName);
		}
		
		return targetMethod;
	}
	
	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}
	
	public void setTargetMethodName(String methodName){
		
		this.targetMethodName = methodName;
	}
	
	public boolean shouldRespondToEventIfApplicable() {
		return respondToEventIfApplicable;
	}
	
	public void setRespondToEventIfApplicable(boolean respondToEventIfApplicable) {
		this.respondToEventIfApplicable = respondToEventIfApplicable;
	}

	public String getNameOfEventToConsume() {
		return nameOfEventToConsume;
	}

	public void setNameOfEventToConsume(String nameOfEventToConsume) {
		this.nameOfEventToConsume = nameOfEventToConsume;
	}

	public String getNameOfEventToProduce() {
		return nameOfEventToProduce;
	}

	public void setNameOfEventToProduce(String nameOfEventToProduce) {
		this.nameOfEventToProduce = nameOfEventToProduce;
	}
	
	static Method getMethodByName(Object object, String methodName){
		
		for(Method m : object.getClass().getMethods()){
			
			// TODO: More robust implementation; something like Spring's MethodMatcher.
			if (m.getName().equals(methodName)){
				
				return m;
			}
		}
		
		return null;
	}
}