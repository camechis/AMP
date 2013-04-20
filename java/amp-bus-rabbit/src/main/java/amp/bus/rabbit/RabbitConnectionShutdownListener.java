package amp.bus.rabbit;


import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.BaseChannelFactory.ConnectionContext;


public class RabbitConnectionShutdownListener implements ShutdownListener {

	private static final Logger logger = LoggerFactory.getLogger(RabbitConnectionShutdownListener.class);
	
	protected BaseChannelFactory channelFactory;
	
	protected ConnectionContext context;
	
	public RabbitConnectionShutdownListener(
			BaseChannelFactory channelFactory,
			ConnectionContext context) {
	
		this.channelFactory = channelFactory;
		this.context = context;
	}

	@Override
	public void shutdownCompleted(ShutdownSignalException ex) {
		
		boolean removed = this.channelFactory.removeConnection(context);
		
		if (removed == false){
			
			logger.warn("Could not find Connection for context '{}' in the pool.", context);
		}
	}

}
