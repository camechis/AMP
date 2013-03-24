package amp.examples.notifier.core;

import java.util.UUID;

import org.joda.time.DateTime;

/**
 * Represents a message/notification to a particular
 * system user.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class UserNotification {

	private String id;
	private String userid;
	private String senderUserId;
	private long timestamp;
	private String message;
	private boolean sendConfirmation = false;
	
	/**
	 * For serialization purposes.  Please set all values.
	 */
	public UserNotification(){}
	
	/**
	 * Instantiate the UserNotification with the appropriate values.
	 * @param userid ID of the User the message is intended for.
	 * @param senderUserId ID of the User/System that sent the message.
	 * @param timestamp When the message was sent.
	 * @param message The Message body.
	 */
	public UserNotification(
			String userid, String senderUserId, long timestamp, String message) {
		
		this.id = UUID.randomUUID().toString();
		this.senderUserId = senderUserId;
		this.userid = userid;
		this.timestamp = timestamp;
		this.message = message;
	}
	
	/**
	 * Instantiate the UserNotification with the appropriate values.  In this case,
	 * the user may desire a confirmation of receipt.  It's up to the individual 
	 * notifier to perform this task.
	 * @param userid ID of the User the message is intended for.
	 * @param senderUserId ID of the User/System that sent the message.
	 * @param timestamp When the message was sent.
	 * @param message The Message body.
	 * @param sendConfirmation Whether or not a confirmation should be sent.
	 */
	public UserNotification(
			String userid, String senderUserId, long timestamp, String message, boolean sendConfirmation) {
		
		this(userid, senderUserId, timestamp, message);
		this.sendConfirmation = sendConfirmation;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the ID of the Intended Recipient.
	 * @return User ID.
	 */
	public String getUserid() {
		return userid;
	}
	
	/**
	 * Set the ID of the Intended Recipient.
	 * @param userid User ID.
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	/**
	 * Get the ID of the User/System that sent the Notification.
	 * @return Sender ID.
	 */
	public String getSenderUserId() {
		return senderUserId;
	}

	/**
	 * Set the ID of the User/System that sent the Notification.
	 * @param senderUserId Sender ID.
	 */
	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	/**
	 * Get the time in which the message was sent.
	 * @return Time sent.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Set the time in which the message was sent.
	 * @param timestamp Time sent.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Get a formatted time (friendly) in which the message was sent.
	 * @return Formatted time sent.
	 */
	public String getDateTime(){
		
		return new DateTime(this.timestamp).toString("E, MMM dd, yyyy hh:mma");
	}
	
	/**
	 * Get the message body.
	 * @return Message body.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the message body.
	 * @param message Message body.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Does the message require confirmation?
	 * @return True if confirmation is necessary.
	 */
	public boolean shouldSendConfirmation() {
		return sendConfirmation;
	}

	/**
	 * Set whether the message requires a confirmation.
	 * @param sendConfirmation True if confirmation is necessary.
	 */
	public void setSendConfirmation(boolean sendConfirmation) {
		this.sendConfirmation = sendConfirmation;
	}

	/**
	 * Convert the state of the Notification to a string.
	 */
	@Override
	public String toString() {
		return "UserNotification [userid=" + userid + ", timestamp="
				+ timestamp + ", message=" + message + ", sendConfirmation="
				+ sendConfirmation + "]";
	}
}