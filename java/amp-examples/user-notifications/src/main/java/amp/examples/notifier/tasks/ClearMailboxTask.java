package amp.examples.notifier.tasks;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.examples.notifier.core.notifiers.NotificationMailbox;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;

/**
 * TASK: Clear the entire Notification Mailbox, removing all messages for
 * all users.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ClearMailboxTask extends Task {

	private static final Logger logger = LoggerFactory.getLogger(ClearMailboxTask.class);
	
	NotificationMailbox notificationMailbox;
	
	/**
	 * Initialize task with access to the mailbox.
	 * @param notificationMailbox Repo of Notifications.
	 */
	public ClearMailboxTask(NotificationMailbox notificationMailbox) {
		
		// This is the task name that is accessible via POST on the admin port:
		// http://hostname:8082/tasks/clear-mailbox
		super("clear-mailbox");
		
		this.notificationMailbox = notificationMailbox;
	}

	/**
	 * Execute the task (i.e. clear the mailbox).
	 */
	@Override
	public void execute(ImmutableMultimap<String, String> params, PrintWriter writer)
			throws Exception {
		
		logger.info("Clearing the mailbox!");
		
		// Clear the mailbox.
		this.notificationMailbox.clearMailbox();
		
		// This will be output to the Response stream.
		writer.println("Mailbox cleared.");
	}
}