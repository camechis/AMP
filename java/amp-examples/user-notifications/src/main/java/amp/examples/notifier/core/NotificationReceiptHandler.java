package amp.examples.notifier.core;

/**
 * Send a receipt of notification.
 * 
 * @author Richard Clayton (Berico Techologies)
 */
public interface NotificationReceiptHandler {

	void sendReceipt(NotificationReceipt receipt);
}