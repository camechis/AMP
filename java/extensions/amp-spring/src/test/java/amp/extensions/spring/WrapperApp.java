package amp.extensions.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import amp.extensions.jaxbs.EventAsSingleParameterWrappingStrategy;
import amp.extensions.jaxbs.MethodRegistration;
import amp.extensions.jaxbs.MethodWrappingStrategy;
import amp.extensions.jaxbs.WrappedMethodRegistry;
import cmf.eventing.patterns.rpc.IRpcEventBus;

public class WrapperApp {
	
	public static void main(String[] args) throws Exception {
		
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("config/ampContext.xml");
		
		IRpcEventBus eventBus = context.getBean(IRpcEventBus.class);
		
		EventAsSingleParameterWrappingStrategy strategy = new EventAsSingleParameterWrappingStrategy(eventBus);
		
		MessageService messageService = new MessageService();
		
		MethodRegistration registration = new MethodRegistration();
		registration.setTarget(messageService);
		registration.setTargetMethodName("printMessageToConsole");
		
		WrappedMethodRegistry registry = new WrappedMethodRegistry();
		registry.setRegistrations(Arrays.asList(registration));
		
		ArrayList<MethodWrappingStrategy> strategies = new ArrayList<MethodWrappingStrategy>();
		strategies.add(strategy);
		
		registry.setStrategies(strategies);
		
		registry.initialize();
		
		Thread.sleep(3000);
		
		for (int i = 0; i < 1000; i++){
			System.out.println(String.format("ITERATION: %s", i));
			eventBus.publish(new ExampleMessage("rich", "hello!", System.currentTimeMillis()));
		}
		
		boolean cont = true;
		Scanner scanner = new Scanner(System.in);
		
		while(cont){
			
			String test = scanner.nextLine();
			if (test.equalsIgnoreCase("exit")) {
				
				cont = false;
			}
		}
		
		scanner.close();
		context.close();
		
	}

}
