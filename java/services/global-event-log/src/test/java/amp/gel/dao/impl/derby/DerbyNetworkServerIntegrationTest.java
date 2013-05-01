package amp.gel.dao.impl.derby;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DerbyNetworkServerIntegrationTest {
	private static final Logger logger = LoggerFactory
			.getLogger(DerbyNetworkServerIntegrationTest.class);

	private DerbyNetworkServer server;

	@Before
	public void setUp() {
		server = new DerbyNetworkServer();
	}

	@Test
	public void testInitialize() {
		try {
			server.start();
		} catch (Exception e) {
			String message = "Unable to start the network server!";
			logger.error(message, e);
			fail(message);
		}

		boolean portTaken = portTaken(DerbyNetworkServer.DEFAULT_PORT);
		assertTrue(portTaken);
	}

	@Test
	public void testDestroy() {
		try {
			server.start();
		} catch (Exception e) {
			String message = "Unable to start the network server!";
			logger.error(message, e);
			fail(message);
		}

		try {
			server.stop();
		} catch (Exception e) {
			String message = "Unable to stop the network server!";
			logger.error(message, e);
			fail(message);
		}

		boolean portTaken = portTaken(DerbyNetworkServer.DEFAULT_PORT);
		assertFalse(portTaken);
	}

	private boolean portTaken(int port) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", port);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}
}
