package amp.rabbit.connection;


import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RabbitConnectionShutdownListener implements ShutdownListener {

	private static final Logger logger = LoggerFactory.getLogger(RabbitConnectionShutdownListener.class);
	
	protected BaseConnectionFactory channelFactory;
	
	//TODO: JM Complete refactor -- context is not what's listened to.
	protected ConnectionContext context;
	
	public RabbitConnectionShutdownListener(
			BaseConnectionFactory channelFactory,
			ConnectionContext context) {
	
		this.channelFactory = channelFactory;
		this.context = context;
	}

	@Override
	public void shutdownCompleted(ShutdownSignalException ex) {
		
	//TODO: JM Restore this-- context isn't exactly what we listen to here...
	//	boolean removed = this.channelFactory.removeConnection(context);
		boolean removed = false;
		if (removed == false){
			
			logger.warn("Could not find Channel for exchange '{}' in the pool.", context);
		}
	}

}
