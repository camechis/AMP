package amp.esp.service;

import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;

public class ESPService extends SpringService<ESPConfiguration> {

	public static void main( String[] args ) throws Exception
	{
		new ESPService().run(args);
	}
	    
	@Override
	public void initialize(Bootstrap<ESPConfiguration> bootstrap) {
	
		super.initialize(bootstrap);
	
		System.out.println("Initializing ESP Service!");
	}	
}