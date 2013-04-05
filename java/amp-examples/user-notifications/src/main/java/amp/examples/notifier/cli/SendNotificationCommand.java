package amp.examples.notifier.cli;

import java.util.List;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import amp.examples.notifier.core.UserNotification;
import cmf.eventing.IEventBus;

import com.berico.fallwizard.SpringConfiguration;

import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;

/**
 * A simple command to send a notification to a user using the EventBus.
 * 
 * This example will pull its configuration from the Spring Context file
 * you are using for the actual NotificationService.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class SendNotificationCommand extends ConfiguredCommand<SpringConfiguration> {

	public SendNotificationCommand() {
		
		super("send", "Prints 'Hello Command' to the console.");
	}

	/**
	 * Register for argument collection from the command line.  In our
	 * case, we want to know who the user is that we will send the
	 * Notification 'to', the 'message' content to send, and who the 
	 * message is 'from'.
	 * @param subparser Inteface for registering commands.
	 */
	@Override
	public void configure(Subparser subparser) {
		
		super.configure(subparser);
		
		// Add the user the message is from.
		subparser.addArgument("from").help("User sending message");
		
		// Add the user we are send to.
		subparser.addArgument("to").help("User receiving message");
		
		// Add the message.  In our case, the "nargs" means allow
		// multiple arguments (separated by whitespace) to be
		// collected for the message.
		subparser.addArgument("message").nargs("*").help("Message");
	}

	/**
	 * Called after Dropwizard initializes, parses the configuration, 
	 * and is ready to execute your command.  In our case, we are
	 * going to fire a message on the bus.
	 * @param bootstrap Dropwizard context
	 * @param namespace Object holding the command line arguments
	 * @param config Configuration object with the locations of our 
	 * Spring context files.
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
		
		// Get the message content from the command line.
		List<String> message = namespace.getList("message");
		
		// Get the user sending the message from the command line.
		String sender = namespace.get("from").toString();
		
		// Get the user receiving the message from the command line.
		String user = namespace.get("to").toString();
		
		// Construct a UserNotification and publish it on the bus.
		eventBus.publish(
			new UserNotification(
				user, // User to send message to.
				sender, // User sending message.
				System.currentTimeMillis(), // Time notification is being sent.
				join(message), // The message; join() simple concatenates the String[]
				true)); // Send a confirmation over the bus
		
		// Close the event bus.
		eventBus.dispose();
		
		// Close the Spring context.
		context.close();
	}
	
	/**
	 * Joins a List of String into a single String separated by spaces.
	 * @param message Message as a list of strings.
	 * @return A single concatenated string.
	 */
	private static String join(List<String> message){
		
		StringBuilder sb = new StringBuilder();
		
		for(String word : message){
		
			sb.append(word).append(" ");
		}
		
		return sb.toString().trim();
	}
}