package amp.rabbit.connection;

import java.io.IOException;

import com.rabbitmq.client.Channel;

public interface IConnectionManager {

	public abstract Channel createChannel() throws IOException,
			InterruptedException;

	public abstract void addConnectionEventHandler(IConnectionEventHandler handler);

	public abstract void removeConnectionEventHandler(IConnectionEventHandler handler);

	public abstract void dispose();

}