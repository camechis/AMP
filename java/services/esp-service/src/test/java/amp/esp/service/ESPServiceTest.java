package amp.esp.service;

import org.junit.Before;
import org.junit.Test;

import amp.esp.service.health.ESPHealthCheck;
import amp.esp.service.resources.ESPResource;
import amp.esp.service.tasks.ESPTask;
import com.yammer.dropwizard.config.Environment;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ESPServiceTest {

    private final Environment environment = mock(Environment.class);
    private final ESPService service = new ESPService();
    private final ESPConfiguration config = new ESPConfiguration();

    @Before
    public void setup() throws Exception {
    	
    		config.setESPProperty("ESP");
    		
    		config.setShouldUseSpringSecurity(true);
    		
    		config.setApplicationContext(new String[]{
    			"conf/applicationContext.xml",
    			"conf/basicAuthSecurityContext.xml"
    		});
    }
    
    @Test
    public void ensure_my_stuff_was_added() throws Exception {
    	
    		service.run(config, environment);
    		
    		verify(environment).addResource(any(ESPResource.class));
    		verify(environment).addTask(any(ESPTask.class));
    		verify(environment).addHealthCheck(any(ESPHealthCheck.class));
    }
}
