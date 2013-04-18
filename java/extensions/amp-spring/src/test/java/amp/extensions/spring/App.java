package amp.extensions.spring;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import amp.topology.client.GlobalTopologyService;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("src/test/resources/ampNamespaceContextTest.xml");
			
		GlobalTopologyService gts = context.getBean(GlobalTopologyService.class);
		
		System.out.println(gts);
		
		context.close();
	}

}
