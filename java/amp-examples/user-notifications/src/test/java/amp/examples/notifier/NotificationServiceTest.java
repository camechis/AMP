package amp.examples.notifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import amp.examples.notifier.health.MailboxFullHealthCheck;
import amp.examples.notifier.health.MailboxMessageCountHealthCheck;
import amp.examples.notifier.resources.NotificationMailboxResource;
import amp.examples.notifier.tasks.ClearMailboxTask;

import com.berico.fallwizard.SpringConfiguration;

import com.yammer.dropwizard.config.Environment;

import static org.mockito.Mockito.*;

/**
 * Verifies resources are instantiated and injected 
 * (from the Spring Context) into the Dropwizard context as expected.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class NotificationServiceTest {

	private final Environment environment = mock(Environment.class);
    private final NotificationService service = new NotificationService();
    private final SpringConfiguration config = new SpringConfiguration();
	
    /**
     * Set up the configuration manually.
     * @throws Exception
     */
	@Before
	public void setup() throws Exception {
		
		config.setShouldUseSpringSecurity(true);
		
		// Config files expected.
		config.setApplicationContext(new String[]{
			"conf/applicationContext.xml",
			"conf/basicAuthSecurityContext.xml",
			"conf/eventBusContext.xml"
		});
	}

	/**
	 * Verify the environment was set up correctly.
	 * @throws Exception
	 */
	@Test
	public void ensure_my_stuff_was_added() throws Exception {

		service.run(config, environment);
		
		verify(environment).addResource(isA(NotificationMailboxResource.class));
		verify(environment).addTask(isA(ClearMailboxTask.class));
		verify(environment).addHealthCheck(isA(MailboxFullHealthCheck.class));
		verify(environment).addHealthCheck(isA(MailboxMessageCountHealthCheck.class));
	}
}
