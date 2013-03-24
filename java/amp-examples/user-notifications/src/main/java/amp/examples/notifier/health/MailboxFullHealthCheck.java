package amp.examples.notifier.health;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import amp.examples.notifier.core.UserNotification;
import amp.examples.notifier.core.notifiers.NotificationMailbox;

import com.yammer.metrics.core.HealthCheck;

/**
 * HEALTH CHECK:  Are there too many full mailboxes in the system?
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class MailboxFullHealthCheck extends HealthCheck {

	public static int MAX_MESSAGES_PER_MAILBOX = 3;
	public static int MAX_FULL_MESSAGE_BOXES = 3;
	
	NotificationMailbox notificationMailbox;
	
	/**
	 * Initialize with a handle on the Notification Mailbox
	 * @param notificationMailbox Notification Repository.
	 */
	public MailboxFullHealthCheck(NotificationMailbox notificationMailbox) {
		
		// This is the name of the Health Check that will appear in the admin console.
		super("full mailboxes");
		
		this.notificationMailbox = notificationMailbox;
	}

	/**
	 * Check to see if the system is healthy.
	 */
	@Override
	protected Result check() throws Exception {
		
		// All the users with full mailboxes
		List<String> usersWithFullMailboxes = new ArrayList<String>();

		// Iterate over the mailboxes
		for(Entry<String, List<UserNotification>> mailbox : 
			this.notificationMailbox.getAllNotifications().entrySet()){
			
			// If the amount of notifications in this mailbox exceeds
			// the limit per mailbox
			if (mailbox.getValue().size() > MAX_MESSAGES_PER_MAILBOX){
				
				// Add the user
				usersWithFullMailboxes.add(mailbox.getKey());
			}
		}
		
		// If the number of full mailboxes exceeds the system's limit,
		// the system is unhealthy.
		return (usersWithFullMailboxes.size() > MAX_FULL_MESSAGE_BOXES)?
			Result.unhealthy(usersWithFullMailboxes.toString()) :
			Result.healthy();
	}
}
