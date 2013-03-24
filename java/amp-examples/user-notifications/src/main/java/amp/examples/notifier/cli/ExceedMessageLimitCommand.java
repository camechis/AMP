package amp.examples.notifier.cli;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import net.sourceforge.argparse4j.inf.Namespace;

import amp.examples.notifier.core.UserNotification;
import amp.examples.notifier.health.MailboxMessageCountHealthCheck;
import cmf.eventing.IEventBus;

import com.berico.fallwizard.SpringConfiguration;
import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;

/**
 * Creates a condition in which the MailboxMessageCountHealthCheck will return 'unhealthy'.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ExceedMessageLimitCommand extends ConfiguredCommand<SpringConfiguration> {

	/**
	 * Initialize the command.
	 */
	public ExceedMessageLimitCommand() {
		
		super("exceed-limit", "Exceed the message limit, causing an 'UNHEALTHY' condition.");
	}

	/**
	 * Exceed the message limit of the server.
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
		
		// Iterate 'N' times, where 'N' is 1 + the message limit
		for(int i = 0; i < MailboxMessageCountHealthCheck.UNHEALTHY_NUMBER_OF_MESSAGES + 1; i++){

			// Construct a UserNotification and publish it on the bus.
			eventBus.publish(
				new UserNotification(
					"user1",
					"fillMailboxUser",
					System.currentTimeMillis(),
					String.format("Test message %s", i)));
		}
		
		// Close the event bus.
		eventBus.dispose();
		
		// Close the Spring context.
		context.close();
		
		// Exit the application (this is necessary because Dropwizard is designed to
		// stay running until terminated.
		System.exit(0);
	}

}
