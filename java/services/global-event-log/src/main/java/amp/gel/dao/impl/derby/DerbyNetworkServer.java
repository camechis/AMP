package amp.gel.dao.impl.derby;

import java.net.InetAddress;

import org.apache.derby.drda.NetworkServerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Derby can be run in what is called the "embedded server" mode. This allows an
 * application to load the embedded JDBC driver for its own use and start the
 * Network Server to allow remote access by applications running in other JVMs.
 * Other applications, such as ij, can connect to the same database via a client
 * JDBC driver.
 * 
 * Refer to the "Embedded Server" section of
 * http://db.apache.org/derby/papers/DerbyTut/ns_intro.html
 * 
 */
public class DerbyNetworkServer {
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

	public void start() throws Exception {
		server = new NetworkServerControl(InetAddress.getByName("localhost"),
				port);
		server.start(null);

		logger.info("Started Derby network server on port " + port);
	}

	public void stop() throws Exception {
		try {
			server.shutdown();
		} finally {
			logger.info("Shutdown Derby network server.");
		}
	}
}
