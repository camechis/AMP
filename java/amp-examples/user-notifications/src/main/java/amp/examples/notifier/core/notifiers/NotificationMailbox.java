package amp.examples.notifier.core.notifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import amp.examples.notifier.core.NotificationReceipt;
import amp.examples.notifier.core.NotificationReceiptHandler;
import amp.examples.notifier.core.Notifier;
import amp.examples.notifier.core.UserNotification;

/**
 * Internally queues UserNotifications (in a pseudo, non-persistent mailbox)
 * for access by users (via some external interface: REST Service, web page).
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class NotificationMailbox implements Notifier {

	/**
	 * Our in-memory mailbox.
	 */
	private ConcurrentHashMap<String, List<UserNotification>> 
		mailroom = new ConcurrentHashMap<String, List<UserNotification>>();
	
	/**
	 * Collection of Receipt Handlers.
	 */
	private ArrayList<NotificationReceiptHandler> 
		receiptHandlers = new ArrayList<NotificationReceiptHandler>();
	
	/**
	 * Initialize the Notification Mailbox (no receipt handlers set).
	 */
	public NotificationMailbox(){}
	
	/**
	 * Initialize with a set of receipt handlers.
	 * @param receiptHandlers Receipt Handlers
	 */
	public NotificationMailbox(
			Collection<NotificationReceiptHandler> receiptHandlers) {
		
		this.setReceiptHandlers(receiptHandlers);
	}
	
	/**
	 * Add the collection of handlers to the internal collection of handlers.
	 * 
	 * This does not clear the existing set of handlers.
	 * 
	 * @param receiptHandlers Receipt Handlers to add.
	 */
	public void setReceiptHandlers(Collection<NotificationReceiptHandler> receiptHandlers) {
		
		this.receiptHandlers.addAll(receiptHandlers);
	}

	/**
	 * Send a Notification to the user.  Really, this just queues the message
	 * in the user's mailbox.
	 */
	@Override
	public void sendNotification(UserNotification userNotification) {
		
		addNotification(userNotification);
	}
	
	/**
	 * Add a notification to the user's mailbox.
	 * @param userNotification Notification to add.
	 */
	public void addNotification(UserNotification userNotification){
		
		// If there's not a mailbox, add one.
		this.mailroom.putIfAbsent(
				userNotification.getUserid(), 
				new ArrayList<UserNotification>());
		
		// Add the notification to the mailbox.
		this.mailroom.get(
			userNotification.getUserid())
				.add(userNotification);
	}
	
	/**
	 * Get all notifications for a User.
	 * @param userid User that needs notifications.
	 * @return List of Notifications.
	 */
	public List<UserNotification> getNotificationsFor(String userid){
		
		List<UserNotification> mail = this.mailroom.get(userid);
		
		return mail;
	}
	
	/**
	 * Acknowledge the Receipt of a message.
	 * @param userid ID of the User who had the notification.
	 * @param notificationId ID of the Notification.
	 * @return True if the notification existed.
	 */
	public boolean acknowledgeReceipt(String userid, String notificationId){
		
		List<UserNotification> mail = this.mailroom.get(userid);
		
		// If the mailbox exists...
		if (mail != null && mail.size() > 0){
			
			UserNotification targetNotification = null;
			
			// Find the UserNotification
			for (UserNotification notification : mail){
				
				if (notification.getId().equalsIgnoreCase(notificationId)){
					
					targetNotification = notification;
				}
			}
			
			// If the UserNotification exists
			if (targetNotification != null){
				
				// Remove the UserNotification from the mailbox.
				mail.remove(targetNotification);
				
				// If the UserNotification needs to send a confirmation.
				if (targetNotification.shouldSendConfirmation()){
					
					// Construct a message receipt
					NotificationReceipt receipt = 
						new NotificationReceipt(
							targetNotification.getSenderUserId(),
							System.currentTimeMillis(),
							targetNotification);
					
					// Iterator over the receipt handlers
					for (NotificationReceiptHandler handler : receiptHandlers){
						
						// And send confirmation.
						handler.sendReceipt(receipt);
					}
				}
				
				// Yes, the Notification was found.
				return true;
			}
		}
		// Notification not found.
		return false;
	}
	
	/**
	 * Get all notifications in the entire system.
	 * @return An unmodifiable copy of the mailbox.
	 */
	public Map<String, List<UserNotification>> getAllNotifications(){
		
		return Collections.unmodifiableMap(this.mailroom);
	}
	
	/**
	 * Clear notifications for a specific user.
	 * @param userid User to clear notifications.
	 */
	public void clearNotificationsFor(String userid){
		
		this.mailroom.remove(userid);
	}
	
	/**
	 * Clear all notifications in the system.
	 */
	public void clearMailbox(){
		
		this.mailroom.clear();
	}
}
