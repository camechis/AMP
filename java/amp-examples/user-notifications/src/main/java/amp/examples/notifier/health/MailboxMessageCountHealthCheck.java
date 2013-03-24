package amp.examples.notifier.health;

import java.util.List;

import amp.examples.notifier.core.UserNotification;
import amp.examples.notifier.core.notifiers.NotificationMailbox;

import com.yammer.metrics.core.HealthCheck;

/**
 * HEALTH CHECK:  Are there too many messages in the system?
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class MailboxMessageCountHealthCheck extends HealthCheck {

	public static int UNHEALTHY_NUMBER_OF_MESSAGES = 25;
	
	NotificationMailbox notificationMailbox;
	
	/**
	 * Initialize with an instance of the Notification Mailbox
	 * @param notificationMailbox Essentially a repo for Notifications
	 */
	public MailboxMessageCountHealthCheck(NotificationMailbox notificationMailbox) {
		
		// This is the name of the Health Check that will appear in the admin console.
		super("overall message count");
		
		this.notificationMailbox = notificationMailbox;
	}

	/**
	 * Check to see if the system is healthy (doesn't have too many messages).
	 */
	@Override
	protected Result check() throws Exception {
		
		// Running total.
		int numberOfMessages = 0;
		
		// Iterate over all mailboxes
		for(List<UserNotification> notificationsPerUser : 
			this.notificationMailbox.getAllNotifications().values()){
			
			// Add the messages per mailbox to our running total.
			numberOfMessages += notificationsPerUser.size();
		}
		
		// If the number of messages in the system exceeds our limit,
		// the system is unhealthy.
		return (numberOfMessages > UNHEALTHY_NUMBER_OF_MESSAGES) ?
			Result.unhealthy(
				String.format("too many messages: %s", numberOfMessages)) :
			Result.healthy();
	}	
}