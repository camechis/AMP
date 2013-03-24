package amp.examples.notifier.views;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import amp.examples.notifier.core.UserNotification;

import com.yammer.dropwizard.views.View;

/**
 * This is an extension of the Dropwizard facility to allow
 * a really simple rendering of a template with your model objects.
 * 
 * There is a much better facility for providing static assets 
 * (i.e. web pages, javascript, css) using the StaticAssetsBundle.
 * 
 * If you simply want to show the state of your objects in HTML
 * format, you can bind them with a template framework like FreeMarker (ftl)
 * or Mustache.
 * 
 * In this case, we are going to display a list of Notifications to the user.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class MailboxView extends View {

	private List<UserNotification> notifications;
	private UserDetails userDetails;
	
	/**
	 * Initialize the view with the model
	 * @param notifications Notifications to be displayed.
	 * @param userDetails User who owns the notifications.
	 */
	public MailboxView(List<UserNotification> notifications, UserDetails userDetails) {
		
		// This specifies the template to use.  It is located relative to the 
		// "src/main/resources" directory, in a folder corresponding to the view's
		// namespace.  In our case, that is "src/main/resources/amp/examples/notifier/views".
		super("mailbox.ftl");
		
		this.notifications = notifications;
		this.userDetails = userDetails;
	}

	/**
	 * Get the notifications (this will allow the underlying template framework
	 * to access them).
	 * @return List of notifications.
	 */
	public List<UserNotification> getNotifications() {
		
		return notifications;
	}

	/**
	 * Does the user have mail?
	 * @return True if there are at least 1 notification.
	 */
	public boolean getHasMail(){
		
		return this.notifications.size() > 0;
	}
	
	/**
	 * Get the User object (this will allow the underlying template framework
	 * to access it).
	 * @return User representation
	 */
	public UserDetails getUserDetails() {
	
		return userDetails;
	}
}