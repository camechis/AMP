package amp.examples.notifier.core;

/**
 * When a UserNotification is received, do whatever you do 
 * (phone call, email, carrier pigeon) to deliver the message.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public interface Notifier {

	/**
	 * Send the Notification.
	 * @param userNotification Notification to send.
	 */
	void sendNotification(UserNotification userNotification);
	
}