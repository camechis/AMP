package amp.gel.dao.impl.derby;

import java.net.InetAddress;

import org.apache.derby.drda.NetworkServerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class DerbyNetworkServer implements InitializingBean {
	private static final Logger logger = LoggerFactory
			.getLogger(DerbyNetworkServer.class);

	private NetworkServerControl server;

	public DerbyNetworkServer() {
		super();
	}

	public void afterPropertiesSet() throws Exception {
		server = new NetworkServerControl(InetAddress.getByName("localhost"),
				1527);
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
