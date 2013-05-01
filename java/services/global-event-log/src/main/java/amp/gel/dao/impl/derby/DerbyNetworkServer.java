package amp.gel.dao.impl.derby;

import java.net.InetAddress;

import org.apache.derby.drda.NetworkServerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class DerbyNetworkServer implements InitializingBean {
	public static final int DEFAULT_PORT = 1527;

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyNetworkServer.class);

	private int port = DEFAULT_PORT;

	public void setPort(int port) {
		this.port = port;
	}

	private NetworkServerControl server;

	public DerbyNetworkServer() {
		super();
	}

	public void afterPropertiesSet() throws Exception {
		server = new NetworkServerControl(InetAddress.getByName("localhost"),
				port);
		server.start(null);

		logger.info("Started Derby network server!");
	}

	public void destroy() throws Exception {
		try {
			server.shutdown();
		} finally {
			logger.info("Shutdown Derby network server!");
		}
	}
}
