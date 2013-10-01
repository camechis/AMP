package amp.rabbit.connection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.transport.RabbitTransportProvider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import cmf.bus.IDisposable;

public class ConnectionManager implements IDisposable {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
	
    private ConnectionFactory _factory;
    private Connection _connection;

    public ConnectionManager(ConnectionFactory factory) throws IOException {
        _factory = factory;
        _connection = _factory.newConnection();
    }

    public Channel createChannel() throws IOException{
        return _connection.createChannel();
    }

    @Override
	public void dispose() {
		if(_connection != null){
			try {
				_connection.close();
			} catch (IOException e) {
				LOG.warn("Error while closing connection.", e);
			}
		}
	}
}
