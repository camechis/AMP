package amp.extensions.spring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import amp.topology.client.GlobalTopologyService;

public class GtsElementIntegrationTest {

	//@Test
	public void test() {
		
		FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("src/test/resources/ampNamespaceContextTest.xml");
			
			GlobalTopologyService gts = context.getBean(GlobalTopologyService.class);
			
			System.out.println(gts);
			
			context.close();
	}

}
