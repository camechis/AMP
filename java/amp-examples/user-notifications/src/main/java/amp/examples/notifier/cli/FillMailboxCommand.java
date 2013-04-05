package amp.examples.notifier.cli;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import net.sourceforge.argparse4j.inf.Namespace;

import amp.examples.notifier.core.UserNotification;
import amp.examples.notifier.health.MailboxFullHealthCheck;
import cmf.eventing.IEventBus;

import com.berico.fallwizard.SpringConfiguration;
import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;

/**
 * Creates a condition in which the MailboxFullHealthCheck will return 'unhealthy'.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class FillMailboxCommand extends ConfiguredCommand<SpringConfiguration> {

	/**
	 * Initialize the FillMailbox Command
	 */
	public FillMailboxCommand() {
		
		super("fill-mailbox", "Fill the mailbox, causing an 'UNHEALTHY' condition.");
	}
	
	/**
	 * Fill the Mailbox!
	 */
	@Override
	protected void run(
			Bootstrap<SpringConfiguration> bootstrap, Namespace namespace,
			SpringConfiguration config) throws Exception {
		
		// Initialize the Spring Context from the files in the configuration.
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext(config.getApplicationContext());
		
		// Pull the event bus from the context.
		IEventBus eventBus = context.getBean(IEventBus.class);
		
		// Iterate 'N' times, where 'N' is 1 + the limit of full message boxes
		for(int i = 0; i < (MailboxFullHealthCheck.MAX_FULL_MESSAGE_BOXES + 1); i++){
			
			// Iterate 'N' times, where 'N' is 1 + the max number of messages per box
			for (int j = 0; j < (MailboxFullHealthCheck.MAX_MESSAGES_PER_MAILBOX + 1); j++){
			
				// Construct a UserNotification and publish it on the bus.
				eventBus.publish(
					new UserNotification(
						String.format("user%s", i),
						"fillMailboxUser",
						System.currentTimeMillis(),
						String.format("Test message %s", j)));
			}
		}
		
		// Close the event bus.
		eventBus.dispose();
		
		// Close the Spring context.
		context.close();
	}

}
