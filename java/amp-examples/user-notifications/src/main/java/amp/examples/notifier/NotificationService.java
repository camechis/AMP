package amp.examples.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.examples.notifier.cli.ExceedMessageLimitCommand;
import amp.examples.notifier.cli.FillMailboxCommand;
import amp.examples.notifier.cli.SendNotificationCommand;

import com.berico.fallwizard.Fallwizard;
import com.berico.fallwizard.SpringConfiguration;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.views.ViewBundle;

/**
 * This is the entry point of the Application.  In our case, 
 * we extend the Fallwizard class which uses the default SpringService
 * configuration and settings.
 * 
 * Normally, this class isn't need (you can specify com.berico.fallwizard.Fallwizard
 * as your main class).  But we need to add a Command and a Bundle.  Unfortunately, 
 * these can't be added programmatically using the SpringService since the context
 * is not initialized until after Dropwizard initializes (thus we can pull the 
 * application contexts from the SpringConfiguration object).  Therefore, you
 * simply need to extend Fallwizard, override initialization, and add your Commands
 * and Bundles.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class NotificationService extends Fallwizard {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	public static void main( String[] args ) throws Exception
    {
    		new NotificationService().run(args);
    }
	
	/**
	 * Initialize the Application, adding the ViewBundle and appropriate
	 * commands.
	 */
	@Override
	public void initialize(Bootstrap<SpringConfiguration> bootstrap) {
		
		logger.info("Initializing Notification Manager.");
		
		// We will add a simple command for sending a message
		// to a user from the Command Line (shell).
		bootstrap.addCommand(new SendNotificationCommand());
		
		// A simple command to fill the mailbox.
		bootstrap.addCommand(new FillMailboxCommand());
		
		// A simple command to exceed the message limit.
		bootstrap.addCommand(new ExceedMessageLimitCommand());
		
		// We will use this to render a template (HTML view) of 
		// the notification mailbox.
		bootstrap.addBundle(new ViewBundle());
		
		super.initialize(bootstrap);
	}
}