package amp.anubis;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnubisService extends SpringService<AnubisConfiguration> {
	
	private static final Logger LOG = LoggerFactory.getLogger(AnubisService.class);


    public static void main(String args[]) {

        try {

            new AnubisService().run(args);
        }
        catch (Exception ex) {
            LOG.error("Exception while starting the Anubis Service.", ex);
        }
    }


    @Override
    public void initialize(Bootstrap<AnubisConfiguration> bootstrap) {

        super.initialize(bootstrap);

        bootstrap.setName("anubis-service");

        bootstrap.addBundle(
                new ConfiguredAssetsBundle("/assets", "/"));
    }
}
