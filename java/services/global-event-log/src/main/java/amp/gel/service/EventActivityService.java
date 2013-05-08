package amp.gel.service;

import org.joda.time.DateTime;

import amp.gel.dao.EventDao;
import amp.gel.dao.TypeDao;
import amp.gel.dao.UserDao;
import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public class EventActivityService {

	private EventDao eventDao;

	private TypeDao typeDao;

	private UserDao userDao;

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public Table getEventsByTime(DateTime start, DateTime stop,
			TimeScale timeScale) throws Exception {
		Table table = eventDao.getEventsByTime(start, stop, timeScale);
		return table;
	}

	public Table getEventsByType(DateTime start, DateTime stop)
			throws Exception {
		Table table = typeDao.getEventsByType(start, stop);
		return table;
	}

	public Table getEventsByUser(DateTime start, DateTime stop)
			throws Exception {
		Table table = userDao.getEventsByUser(start, stop);
		return table;
	}
}
