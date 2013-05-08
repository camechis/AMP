package amp.gel.dao;

import org.joda.time.DateTime;

import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public interface EventDao {

	Table getEventsByTime(DateTime start, DateTime stop, TimeScale timeScale)
			throws Exception;

	Table getEventsByTimeForUser(DateTime start, DateTime stop,
			TimeScale timeScale, String user) throws Exception;

	Table getEventsByTimeForType(DateTime start, DateTime stop,
			TimeScale timeScale, String type) throws Exception;

}