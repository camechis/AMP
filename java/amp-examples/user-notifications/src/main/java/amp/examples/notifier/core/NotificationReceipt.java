package amp.examples.notifier.core;

import java.util.UUID;

import org.joda.time.DateTime;

/**
 * Represents a confirmation for a UserNotification.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class NotificationReceipt {

	private String receiptId;
	private String userRequiringReceipt;
	private long timestamp;
	private UserNotification originalNotification;
	
	/**
	 * Please do not call this; only for serializers.
	 */
	public NotificationReceipt(){}
	
	/**
	 * Create a message receipt.
	 * @param userRequiringReceipt
	 * @param timestamp
	 * @param originalNotification
	 */
	public NotificationReceipt(
			String userRequiringReceipt,
			long timestamp, UserNotification originalNotification) {
		
		this.receiptId = UUID.randomUUID().toString();
		this.userRequiringReceipt = userRequiringReceipt;
		this.timestamp = timestamp;
		this.originalNotification = originalNotification;
	}

	/**
	 * Get the Id of this receipt.
	 * @return ID of receipt.
	 */
	public String getReceiptId() {
		return receiptId;
	}
	
	/**
	 * Set the Id of this receipt.
	 * @param receiptId ID of receipt.
	 */
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	
	
	/**
	 * Get the ID of the User requiring a receipt.
	 * @return ID of the User requiring a receipt.
	 */
	public String getUserRequiringReceipt() {
		return userRequiringReceipt;
	}
	
	/**
	 * Get the ID of the User requiring a receipt.
	 * @param userRequiringReceipt ID of the User requiring a receipt.
	 */
	public void setUserRequiringReceipt(String userRequiringReceipt) {
		this.userRequiringReceipt = userRequiringReceipt;
	}
	
	/**
	 * Get the Original Notification
	 * @return Original Notification
	 */
	public UserNotification getOriginalNotification() {
		return originalNotification;
	}

	/**
	 * Set the Original Notification
	 * @param originalNotification Original Notification
	 */
	public void setOriginalNotification(UserNotification originalNotification) {
		this.originalNotification = originalNotification;
	}

	/**
	 * Get the time the receipt was sent.
	 * @return Time receipt was sent
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Get a formatted time (friendly) in which the receipt was sent.
	 * @return Formatted time sent.
	 */
	public String getDateTime(){
		
		return new DateTime(this.timestamp).toString("E, MMM dd, yyyy hh:mma");
	}
	
	/**
	 * Set the time the receipt was sent.
	 * @param timestamp Time receipt was sent
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Dump the state of the receipt to a string
	 * @return String representation of the state.
	 */
	@Override
	public String toString() {
		return "NotificationReceipt [receiptId=" + receiptId
				+ ", userRequiringReceipt=" + userRequiringReceipt
				+ ", timestamp=" + timestamp + ", originalNotification="
				+ originalNotification + "]";
	}
	
}