package amp.examples.notifier.core.notifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.examples.notifier.core.Notifier;
import amp.examples.notifier.core.UserNotification;

/**
 * Prints the UserNotification to the console when received.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ConsoleNotifier implements Notifier {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleNotifier.class);
	
	/**
	 * Print the UserNotification to the console.
	 * @param userNotification UserNotification to print.
	 */
	@Override
	public void sendNotification(UserNotification userNotification) {
		
		logger.info("{}", userNotification);
	}	
}