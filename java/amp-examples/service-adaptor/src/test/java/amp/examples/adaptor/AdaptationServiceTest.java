package amp.examples.adaptor;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.berico.fallwizard.SpringConfiguration;
import com.yammer.dropwizard.config.Environment;

public class AdaptationServiceTest {

	private final Environment environment = mock(Environment.class);
    private final AdaptationService service = new AdaptationService();
    private final SpringConfiguration config = new SpringConfiguration();

    @Before
    public void setup() throws Exception {
    		
    		config.setShouldUseSpringSecurity(true);
    		
    		config.setApplicationContext(new String[]{
    			"conf/applicationContext.xml",
    			"conf/basicAuthSecurityContext.xml"
    		});
    }
    
    @Test
    public void ensure_my_stuff_was_added() throws Exception {
    	
    		//service.run(config, environment);
    		
    		//verify(environment).addResource(any(ExampleResource.class));
    		//verify(environment).addTask(any(ExampleTask.class));
    		//verify(environment).addHealthCheck(any(ExampleHealthCheck.class));
    }
}
