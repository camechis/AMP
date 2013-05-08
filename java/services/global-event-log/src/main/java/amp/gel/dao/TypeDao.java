package amp.gel.dao;

import java.util.List;

import org.joda.time.DateTime;

import amp.gel.domain.Table;

public interface TypeDao {

	Table getEventsByType(DateTime start, DateTime stop) throws Exception;

	Table getEventsByTypeForUser(DateTime start, DateTime stop, String user)
			throws Exception;

	List<String> getTypes(DateTime start, DateTime stop);

}