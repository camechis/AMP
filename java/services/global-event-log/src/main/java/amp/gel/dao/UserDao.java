package amp.gel.dao;

import java.util.List;

import org.joda.time.DateTime;

import amp.gel.domain.Table;

public interface UserDao {

	/**
	 * Get count of events between the start and stop dates by their user. For
	 * each user, the table includes a row for the count.
	 */
	Table getEventsByUser(DateTime start, DateTime stop) throws Exception;

	/**
	 * Get count of events between the start and stop dates by their user for
	 * the given type. For each user, the table includes a row for the count.
	 */
	Table getEventsByUserForType(DateTime start, DateTime stop, String type)
			throws Exception;

	/**
	 * Get a list of users for all events between the start and stop dates.
	 */
	List<String> getUsers(DateTime start, DateTime stop);

}