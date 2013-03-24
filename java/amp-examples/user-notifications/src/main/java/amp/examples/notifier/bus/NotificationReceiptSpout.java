package amp.examples.notifier.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.eventing.IEventBus;
import amp.examples.notifier.core.NotificationReceipt;
import amp.examples.notifier.core.NotificationReceiptHandler;

/**
 * Receipt Handler that publishes receipts on the bus (assuming
 * some third-party receives receipts that way.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class NotificationReceiptSpout implements NotificationReceiptHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationReceiptSpout.class);
	
	IEventBus eventBus;
	
	/**
	 * Initialize with instance of the event bus.
	 * @param eventBus a preconfigured Event Bus.
	 */
	public NotificationReceiptSpout(IEventBus eventBus){
		
		this.eventBus = eventBus;
	}
	
	/**
	 * Send receipt over the bus.
	 */
	@Override
	public void sendReceipt(NotificationReceipt receipt) {
		
		logger.info("Sending receipt: {}", receipt);
	
		try {
			
			// Publish the receipt
			this.eventBus.publish(receipt);
			
		} catch (Exception e) {
			
			logger.error("Could not publish receipt on bus: {}", e);
		}
	}

}
