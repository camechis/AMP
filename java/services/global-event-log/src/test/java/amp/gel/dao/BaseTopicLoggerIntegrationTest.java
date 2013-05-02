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

	protected static final String TOPIC = SimplePojo.class.getCanonicalName();

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

		try {
			/*
			 * Sleep to give envelope bus time to register. This can be removed
			 * after the AMP "bug" has been fixed.
			 */
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing
		}

		for (int i = 0; i < NUM_EVENTS; i++) {
			try {
				eventBus.publish(new SimplePojo("Michael Jordan", i));
			} catch (Exception e) {
				String message = "Unable to publish messages";
				logger.error(message, e);
				fail(message);
			}
		}

		while (topicCounter.getCount() < NUM_EVENTS) {
			try {
				logger.info("# of events: " + topicCounter.getCount());
				logger.info("Missing envelopes: "
						+ topicCounter.getMissingIds());

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
		private String text;

		private int id;

		public SimplePojo(String text, int id) {
			super();
			this.text = text;
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "SimplePojo [text=" + text + ", id=" + id + "]";
		}
	}
}