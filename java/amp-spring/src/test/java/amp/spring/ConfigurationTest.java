package amp.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationTest {

	public static void main( String[] args ) {
		
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
		
	}
	
	
}
