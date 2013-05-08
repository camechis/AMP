package amp.gel.service;

import java.util.List;

import org.joda.time.DateTime;

import amp.gel.dao.EventDao;
import amp.gel.dao.TypeDao;
import amp.gel.dao.UserDao;
import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public class UserActivityService {
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
			TimeScale timeScale, String user) throws Exception {
		Table table = eventDao.getEventsByTimeForUser(start, stop, timeScale,
				user);
		return table;
	}

	public Table getEventsByType(DateTime start, DateTime stop, String user)
			throws Exception {
		Table table = typeDao.getEventsByTypeForUser(start, stop, user);
		return table;
	}

	public List<String> getUsers(DateTime start, DateTime stop) {
		List<String> users = userDao.getUsers(start, stop);
		return users;
	}
}
