package amp.extensions.jaxbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WrappedMethodRegistry {
	
	ArrayList<MethodRegistration> registrations =  new ArrayList<MethodRegistration>();
	ArrayList<MethodWrappingStrategy> strategies = new ArrayList<MethodWrappingStrategy>();
	HashMap<Class<?>, Object> registeredInjects = new HashMap<Class<?>, Object>();
	
	public WrappedMethodRegistry(){}

	public WrappedMethodRegistry(
			Collection<MethodRegistration> registrations,
			Collection<MethodWrappingStrategy> strategies,
			Map<Class<?>, Object> registeredInjects) {
		
		setRegisteredInjects(registeredInjects);
		setRegistrations(registrations);
		setStrategies(strategies);
	}

	public void initialize() throws Exception {
		
		for (MethodRegistration registration : this.registrations){
			
			WrappedMethodContext context = new WrappedMethodContext(registration, this.registeredInjects);
			
			for (MethodWrappingStrategy strategy : this.strategies){
				
				if (strategy.isAppropriateStrategy(context)){
					
					strategy.wrapMethod(context);
					
					break;
				}
			}
		}
	}

	public void setRegistrations(Collection<MethodRegistration> registrations) {
		
		this.registrations.addAll(registrations);
	}
	
	public void setStrategies(Collection<MethodWrappingStrategy> strategies) {
		
		System.out.println("STRATEGIES: " + strategies);
		
		this.strategies.addAll(strategies);
	}

	public void setRegisteredInjects(Map<Class<?>, Object> registeredInjects) {
		
		this.registeredInjects.putAll(registeredInjects);
	}
}
