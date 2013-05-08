package amp.gel.dao;

import java.util.List;

import org.joda.time.DateTime;

import amp.gel.domain.Table;

public interface UserDao {

	Table getEventsByUser(DateTime start, DateTime stop) throws Exception;

	Table getEventsByUserForType(DateTime start, DateTime stop, String type)
			throws Exception;

	List<String> getUsers(DateTime start, DateTime stop);

}