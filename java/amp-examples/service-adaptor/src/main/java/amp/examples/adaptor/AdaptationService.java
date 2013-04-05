package amp.examples.adaptor;

import com.berico.fallwizard.SpringConfiguration;
import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.views.ViewBundle;

public class AdaptationService extends SpringService<SpringConfiguration> {

	public static void main( String[] args ) throws Exception
	{
		new AdaptationService().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<SpringConfiguration> bootstrap) {
	
		super.initialize(bootstrap);
		
		bootstrap.addBundle(new ViewBundle());
	}	
}