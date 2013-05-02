package amp.gel.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import amp.gel.service.TopicCounter;
import amp.gel.service.TopicLogger;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventBus;

public class BaseTopicLoggerIntegrationTest {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseTopicLoggerIntegrationTest.class);

	protected static final int NUM_EVENTS = 100;

	protected static final SimplePojo EVENT = new SimplePojo("Michael Jordan",
			23);

	protected static final String TOPIC = EVENT.getClass().getCanonicalName();

	protected static final String TABLE_NAME = "test_envelope";

	@Autowired
	protected IEnvelopeBus envelopeBus;

	@Autowired
	protected IEventBus eventBus;

	@Autowired
	protected DatastoreTestHelper datastoreTestHelper;

	@Autowired
	protected DatastoreWriter datastoreWriter;

	protected TopicLogger topicLogger;

	protected void setUp() throws Exception {
		topicLogger = new TopicLogger();
		topicLogger.setEnvelopeBus(envelopeBus)
				.setDatastoreWriter(datastoreWriter).setTopic(TOPIC);
		topicLogger.start();
	}

	protected void tearDown() throws Exception {
		topicLogger.stop();
	}

	protected void test() {
		TopicCounter topicCounter = new TopicCounter(TOPIC);

		try {
			envelopeBus.register(topicCounter);
		} catch (Exception e) {
			String message = "Unable to register for messages";
			logger.error(message, e);
			fail(message);
		}

		for (int i = 0; i < NUM_EVENTS; i++) {
			try {
				eventBus.publish(EVENT);
			} catch (Exception e) {
				String message = "Unable to publish messages";
				logger.error(message, e);
				fail(message);
			}
		}

		while (topicCounter.getCount() < NUM_EVENTS) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// do nothing
			}
		}

		try {
			long count = datastoreTestHelper.getRowCount(TABLE_NAME);

			assertEquals(NUM_EVENTS, count);
			logger.info("Table '" + TABLE_NAME + "' had " + count + " values");
		} catch (Exception e) {
			String message = "Unable to count entries in table";
			logger.error(message, e);
			fail(message);
		}
	}

	static public class SimplePojo {
		private String attribute1;

		private int attribute2;

		public SimplePojo(String attribute1, int attribute2) {
			super();
			this.attribute1 = attribute1;
			this.attribute2 = attribute2;
		}

		public String getAttribute1() {
			return attribute1;
		}

		public void setAttribute1(String attribute1) {
			this.attribute1 = attribute1;
		}

		public int getAttribute2() {
			return attribute2;
		}

		public void setAttribute2(int attribute2) {
			this.attribute2 = attribute2;
		}

		@Override
		public String toString() {
			return "SimplePojo [attribute1=" + attribute1 + ", attribute2="
					+ attribute2 + "]";
		}
	}
}