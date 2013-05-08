package amp.gel.dao.impl.derby.data;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import amp.gel.domain.TimeScale;

public class TestDerbyDataReader {

	public static void main(String[] args) throws Exception {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"test_data/derby-reader-context.xml");

		DerbyEventDao eventDao = applicationContext.getBean("eventDao",
				DerbyEventDao.class);

		eventDao.getEventsByTime(new DateTime().withMonthOfYear(1),
				new DateTime().withMonthOfYear(5), TimeScale.MONTH);

		eventDao.getEventsByTime(
				new DateTime().withYear(2011).withMonthOfYear(1),
				new DateTime().withMonthOfYear(6), TimeScale.MONTH);

		DerbyTypeDao typeDao = applicationContext.getBean("typeDao",
				DerbyTypeDao.class);

		typeDao.getEventsByType(new DateTime().withMonthOfYear(1),
				new DateTime());
		typeDao.getEventsByTypeForUser(new DateTime().withMonthOfYear(1),
				new DateTime(), "gwashington");

		DerbyUserDao userDao = applicationContext.getBean("userDao",
				DerbyUserDao.class);

		userDao.getEventsByUser(new DateTime().withMonthOfYear(1),
				new DateTime());

		userDao.getEventsByUserForType(new DateTime().withMonthOfYear(1),
				new DateTime(),
				"amp.gel.dao.impl.derby.data.events.core.ExportEvent");
	}
}