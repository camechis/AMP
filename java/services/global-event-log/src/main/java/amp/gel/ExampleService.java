package amp.gel;

import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;

public class ExampleService extends SpringService<ExampleConfiguration> {

	public static void main( String[] args ) throws Exception
	{
		new ExampleService().run(args);
	}
	    
	@Override
	public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
	
		super.initialize(bootstrap);
	
		System.out.println("Initializing Example Service!");
	}	
}