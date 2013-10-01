package amp.rabbit.connection;

import amp.rabbit.dispatch.RabbitListener;


public class ReconnectOnConnectionErrorCallback implements IOnConnectionErrorCallback {

	protected IRabbitConnectionFactory channelFactory = null;

	
	public ReconnectOnConnectionErrorCallback(
			IRabbitConnectionFactory channelFactory) {
		
		this.channelFactory = channelFactory;
	}


	@Override
	public void onConnectionError(RabbitListener listener) {
		
		new ConnectionRemediator(listener, channelFactory).start();
	}
}
