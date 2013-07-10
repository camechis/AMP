package amp.gel.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.gel.dao.DatastoreWriter;
import amp.gel.service.TopicLogger;

import cmf.bus.Envelope;

public class TopicLoggerTest {
	private static final Logger logger = LoggerFactory
			.getLogger(TopicLoggerTest.class);

	private DatastoreWriter datastoreWriter = mock(DatastoreWriter.class);

	private TopicLogger topicLogger;

	@Before
	public void setUp() {
		topicLogger = new TopicLogger();
		topicLogger.setDatastoreWriter(datastoreWriter);
	}

	/**
	 * Test checks that the TopicLogger can continue handling envelopes despite
	 * lower-level exceptions.
	 */
	@Test
	public void testHandleCatchesExceptions() {
		Envelope envelope = mock(Envelope.class);

		try {
			doThrow(new Exception("mock exception")).when(datastoreWriter)
					.write(envelope);
		} catch (Exception e) {
			// do nothing
		}

		String message = "*** Ignore the exception.  It's just part of the test. ***";
		logger.info(message);

		Object result = topicLogger.handle(envelope);

		logger.info(message);

		assertNotNull(result);
		assertTrue(result instanceof Boolean);
		assertTrue(((Boolean) result).booleanValue());
	}
}
