package amp.gel.service;

import java.util.List;

import org.joda.time.DateTime;

import amp.gel.dao.EventDao;
import amp.gel.dao.TypeDao;
import amp.gel.dao.UserDao;
import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

/**
 * Service that provides the activity for a given event type. The method
 * getTypes() should be used first to retrieve a list of valid event types for a
 * given date range. From there, two different tables can be retrieved for event
 * activity for a given event type: events by time and events by their user.
 * 
 */
public class TypeActivityService {
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
			TimeScale timeScale, String type) throws Exception {
		Table table = eventDao.getEventsByTimeForType(start, stop, timeScale,
				type);
		return table;
	}

	public Table getEventsByUser(DateTime start, DateTime stop, String type)
			throws Exception {
		Table table = userDao.getEventsByUserForType(start, stop, type);
		return table;
	}

	public List<String> getTypes(DateTime start, DateTime stop) {
		List<String> types = typeDao.getTypes(start, stop);
		return types;
	}
}
