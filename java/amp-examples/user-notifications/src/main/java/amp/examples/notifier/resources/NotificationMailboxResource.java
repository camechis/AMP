package amp.examples.notifier.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import amp.examples.notifier.core.UserNotification;
import amp.examples.notifier.core.notifiers.NotificationMailbox;
import amp.examples.notifier.views.MailboxView;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

/**
 * REST Endpoint for Accessing the Notification Mailbox
 * 
 * @author Richard Clayton (Berico Technologies)
 */
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationMailboxResource {

	private static final Logger logger = LoggerFactory.getLogger(NotificationMailboxResource.class);
	
	NotificationMailbox notificationMailbox;
	
	/**
	 * Initialize with access to the Notification Mailbox
	 * @param notificationMailbox Notification Mailbox (repository)
	 */
	public NotificationMailboxResource(NotificationMailbox notificationMailbox){
		
		this.notificationMailbox = notificationMailbox;
	}
	
	/**
	 * Clear all mail for the user (given to us by authentication).
	 * @param user User whose mailbox should be cleared.
	 */
	@DELETE
    @Timed
	public void clearMailbox(@Auth UserDetails user){

		this.notificationMailbox.clearNotificationsFor(user.getUsername());
	}
	
	/**
	 * Get all mail for the user (given to us by authentication).
	 * @param user User needing mail.
	 * @return A list of mail.
	 */
	@GET
	@Timed
	public List<UserNotification> getMail(@Auth UserDetails user){

		return grabNotifications(user.getUsername());
	}
	
	/**
	 * Get all mail for the user (given to us by authentication), as a friendly
	 * HTML view.
	 * @param user User needing mail.
	 * @return A list of mail.
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Timed
	public MailboxView getMailAsHtml(@Auth UserDetails user){
		
		return new MailboxView(grabNotifications(user.getUsername()), user);
	}
	
	/**
	 * Encapsulates some common repo and logging actions performed when getting mail.
	 * @param userId User to grab notifications for.
	 * @return User Notifications
	 */
	List<UserNotification> grabNotifications(String userId){
		
		logger.info("Getting mail for {}", userId);
		
		List<UserNotification> mail = this.notificationMailbox.getNotificationsFor(userId);
		
		if (mail == null){
			
			mail = new ArrayList<UserNotification>();
		}
		
		logger.info("Found {} notifications.", mail.size());
		
		return mail;
	}
	
	/**
	 * Acknowledge the receipt of a message.
	 * @param user User acknowledging the message.
	 * @param notificationId ID of the message.
	 * @return Response OK if the message exists, otherwise 404.
	 */
	@POST
	@Timed
	public Response acknowledgeNotification(@Auth UserDetails user, @FormParam("msgId") String notificationId){
		
		logger.info("Acknowledging message {} for {}.", notificationId, user.getUsername());
		
		if (this.notificationMailbox
				.acknowledgeReceipt(
					user.getUsername(), notificationId)){
			
			// OK - 200
			return Response.ok().build();
		}
		
		// NOT FOUND!
		return Response.status(404).build();
	}
}