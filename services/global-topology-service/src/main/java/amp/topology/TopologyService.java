package amp.topology;

import java.util.Map;

import amp.plugin.IConfigurationModifier;
import com.yammer.dropwizard.config.Environment;
import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopologyService extends SpringService<TopologyConfiguration>
{
	private static final Logger logger = LoggerFactory.getLogger(TopologyService.class);
	
    public static void main( String[] args ) throws Exception
    {
    		new TopologyService().run(args);
    }
    
	@Override
	public void initialize(Bootstrap<TopologyConfiguration> bootstrap) {
		
		super.initialize(bootstrap);
		
		bootstrap.setName("global-topology-service");
		
		bootstrap.addBundle(
			new ConfiguredAssetsBundle("/assets/", "/"));
	}

	@Override
	public void run(TopologyConfiguration configuration, 
					Environment environment) throws Exception {
	
		// Run the parent class which will configure our application context.
		super.run(configuration,environment);

		handleConfigMods(configuration);

	}	

	protected void handleConfigMods(TopologyConfiguration config) {
		final Map<String, IConfigurationModifier> beansOfType = applicationContext.getBeansOfType(IConfigurationModifier.class);
        
        for (String beanName : beansOfType.keySet()) {
            
        	IConfigurationModifier configModifier = beansOfType.get(beanName);
            
            logger.info("Ran configuration modification: " + configModifier.getClass().getName());
            configModifier.modifyConfiguration(config);
        }
	}


}
