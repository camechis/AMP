package amp.examples.notifier.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.dropwizard.lifecycle.Managed;

import amp.examples.notifier.core.Notifier;
import amp.examples.notifier.core.UserNotification;

import cmf.bus.Envelope;
import cmf.eventing.IEventBus;
import cmf.eventing.IEventHandler;

/**
 * Receives UserNotifications from the Bus and sends
 * them on all registered Notifiers.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class UserNotificationSink implements Managed {

	private final Logger logger = LoggerFactory.getLogger(UserNotificationSink.class);
	
	private ArrayList<Notifier> notifiers = new ArrayList<Notifier>();
	
	private IEventBus eventBus;
	
	/**
	 * Initialize the Sink with an EventBus instance and a list of notifiers.
	 * @param eventBus
	 * @param notifiers
	 */
	public UserNotificationSink(IEventBus eventBus, List<Notifier> notifiers){
		
		logger.info("Notification Event Sink initialized with: {}", eventBus);
		
		this.eventBus = eventBus;
		this.setNotifiers(notifiers);
	}
	
	/**
	 * Add a list of Notifiers to the Sink.
	 * @param notifiers List of Notifiers
	 */
	public void setNotifiers(List<Notifier> notifiers){
		
		this.notifiers.addAll(notifiers);
	}

	/**
	 * Registers an IEventHandler for UserNotifications.  This is managed by
	 * Dropwizard.
	 */
	@Override
	public void start() throws Exception {
		
		logger.info("Notification service started, connecting to the bus.");
		
		// Register the event handler
		this.eventBus.subscribe(new IEventHandler<UserNotification>(){
			
			/**
			 * The type of object handled by the Handler.
			 */
			@Override
			public Class<UserNotification> getEventType() {
				
				return UserNotification.class;
			}

			/**
			 * When a UserNotification is received, iterate over all Notifiers
			 * sending the notification.
			 * @param userNotification UserNotification received over the bus.
			 * @param headers Headers from the Envelope
			 * @return Any value other than false (or an exception) is considered
			 * an acknowledgement of the receipt of the message.
			 */
			@Override
			public Object handle(
				UserNotification userNotification, Map<String, String> headers) {
				
				for (Notifier notifier : notifiers){
					
					notifier.sendNotification(userNotification);
				}
				
				return true;
			}

			/**
			 * If a failure occurs during the handling of the message, we'll
			 * simply log the error to the console.
			 * @param envelope The envelope that failed.
			 * @param e Exception thrown
			 */
			@Override
			public Object handleFailed(Envelope envelope, Exception e) {
				
				logger.error("Error handling UserNotification: {}", e);
				
				return null;
			}
			
		});
	}

	/**
	 * When the application is terminated, Dropwizard will call this method.
	 * In the case of this class, we want to close (dispose of) the EventBus.
	 */
	@Override
	public void stop() throws Exception {
		
		this.eventBus.dispose();
	}
}
