package amp.gel.dao;

import org.joda.time.DateTime;

import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public interface EventDao {

	/**
	 * Get statistics for events between the start and stop dates and the given
	 * time scale. For each unit of time of the time scale, the table includes a
	 * row for the minimum, average, maximum, sum and count.
	 */
	Table getEventsByTime(DateTime start, DateTime stop, TimeScale timeScale)
			throws Exception;

	/**
	 * Get statistics for events between the start and stop dates for the given
	 * time scale and user. For each unit of time of the time scale, the table
	 * includes a row for the minimum, average, maximum, sum and count.
	 */
	Table getEventsByTimeForUser(DateTime start, DateTime stop,
			TimeScale timeScale, String user) throws Exception;

	/**
	 * Get statistics for events between the start and stop dates for the given
	 * time scale and event type. For each unit of time of the time scale, the
	 * table includes a row for the minimum, average, maximum, sum and count.
	 */
	Table getEventsByTimeForType(DateTime start, DateTime stop,
			TimeScale timeScale, String type) throws Exception;

}