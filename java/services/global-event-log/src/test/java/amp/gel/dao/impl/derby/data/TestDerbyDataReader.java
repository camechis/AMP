package amp.gel.dao.impl.derby.data;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDerbyDataReader {
	private static final Logger logger = LoggerFactory
			.getLogger(TestDerbyDataReader.class);

	public static void main(String[] args) throws Exception {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"test_data/derby-reader-context.xml");

		DerbyReader datastoreReader = applicationContext.getBean(
				"datastoreReader", DerbyReader.class);

		// datastoreReader.getEventsByTime(new DateTime().withMonthOfYear(1),
		// new DateTime().withMonthOfYear(5), TimeScale.MONTH);
		//
		// datastoreReader.getEventsByTime(new DateTime().withYear(2011)
		// .withMonthOfYear(1), new DateTime().withMonthOfYear(6),
		// TimeScale.MONTH);

//		datastoreReader.getEventsByType(new DateTime().withYear(2011)
//				.withMonthOfYear(1), new DateTime());
		
		datastoreReader.getEventsByUser(new DateTime().withYear(2011)
				.withMonthOfYear(1), new DateTime());
	}
}