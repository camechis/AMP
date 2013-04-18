package amp.extensions.spring;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cmf.eventing.IEventBus;
import cmf.eventing.patterns.rpc.IRpcEventBus;


public class JaxBsExampleIntegrationTest {

	@Test
	public void test() throws Exception {
		
		String[] contexts = new String[]{
			"src/test/resources/ampNamespaceContextTest.xml",
			"src/test/resources/ampContext.xml"
		};
		
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext(contexts);
		
		System.out.println("Done initializing.");
		
		Thread.sleep(5000);
		
		IEventBus eventBus = context.getBean(IRpcEventBus.class);
		
		eventBus.publish(new Message("Hello JAX-BS!"));
		
		Thread.sleep(8000);
		
		context.close();
	}

}
