package amp.gel.dao.impl.derby.data;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import amp.gel.dao.DatastoreWriter;
import cmf.bus.Envelope;

public class TestDerbyDataWriter {
	private static final Logger logger = LoggerFactory
			.getLogger(TestDerbyDataWriter.class);

	public static void main(String[] args) throws Exception {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"test_data/derby-context.xml");
		DatastoreWriter datastoreWriter = applicationContext.getBean(
				"datastoreWriter", DatastoreWriter.class);
		EnvelopeGenerator envelopeGenerator = applicationContext.getBean(
				"envelopeGenerator", EnvelopeGenerator.class);

		datastoreWriter.initialize();

		for (Iterator<Envelope> iter = envelopeGenerator.iterator(); iter
				.hasNext();) {
			try {
				Envelope envelope = iter.next();
				datastoreWriter.write(envelope);
			} catch (Exception e) {
				logger.error("Unable to publish event", e);
			}
		}

		datastoreWriter.close();
	}
}