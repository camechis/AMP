package amp.gel.dao;

import java.util.List;

import org.joda.time.DateTime;

import amp.gel.domain.Table;

public interface TypeDao {

	/**
	 * Get count of events between the start and stop dates by their type. For
	 * each event type, the table includes a row for the count.
	 */
	Table getEventsByType(DateTime start, DateTime stop) throws Exception;

	/**
	 * Get count of events between the start and stop dates by their type for
	 * the given user. For each event type, the table includes a row for the
	 * count.
	 */
	Table getEventsByTypeForUser(DateTime start, DateTime stop, String user)
			throws Exception;

	/**
	 * Get a list of event types for all events between the start and stop
	 * dates.
	 */
	List<String> getTypes(DateTime start, DateTime stop);

}