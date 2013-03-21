package amp.topology.client.integration;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import amp.topology.client.GlobalTopologyService;

public class SpringIntegrationTest {
	
	@Test
	public void test_basic_auth_spring_configuration(){
		
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext("exampleBasicAuthContext.xml");
		
		GlobalTopologyService gts = context.getBean(GlobalTopologyService.class);
		
		assertNotNull(gts);
		
		context.close();
	}
	
	@Test
	public void test_x509_spring_configuration(){
		
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext("exampleX509Context.xml");
		
		GlobalTopologyService gts = context.getBean(GlobalTopologyService.class);
		
		assertNotNull(gts);
		
		context.close();
	}
}
