package amp.bus.rabbit;


public interface IOnConnectionErrorCallback {

	void onConnectionError(RabbitListener listener);
}