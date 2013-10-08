package amp.rabbit.connection;

import amp.rabbit.dispatch.RabbitListener;


public interface IOnConnectionErrorCallback {

	void onConnectionError(RabbitListener listener);
}