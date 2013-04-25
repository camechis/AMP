package amp.gel;

import org.junit.Before;
import org.junit.Test;

import amp.gel.health.ExampleHealthCheck;
import amp.gel.resources.ExampleResource;
import amp.gel.tasks.ExampleTask;
import com.yammer.dropwizard.config.Environment;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ExampleServiceTest {

	private final Environment environment = mock(Environment.class);
    private final ExampleService service = new ExampleService();
    private final ExampleConfiguration config = new ExampleConfiguration();

    @Before
    public void setup() throws Exception {
    	
    		config.setExampleProperty("Example");
    		
    		config.setShouldUseSpringSecurity(true);
    		
    		config.setApplicationContext(new String[]{
    			"conf/applicationContext.xml",
    			"conf/basicAuthSecurityContext.xml"
    		});
    }
    
    @Test
    public void ensure_my_stuff_was_added() throws Exception {
    	
    		service.run(config, environment);
    		
    		verify(environment).addResource(any(ExampleResource.class));
    		verify(environment).addTask(any(ExampleTask.class));
    		verify(environment).addHealthCheck(any(ExampleHealthCheck.class));
    }
}
