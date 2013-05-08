package amp.gel.dao.impl.derby.data;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import amp.gel.domain.ColumnHeader;
import amp.gel.domain.ColumnType;
import amp.gel.domain.Row;
import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public class DerbyReader {

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyReader.class);

	private static final String EVENTS_BY_TIME_FOR_MONTH_QUERY = "SELECT "
			+ "MIN(count), AVG(CAST(count AS DOUBLE PRECISION)), MAX(count), SUM(count), COUNT(count) "
			+ "FROM ("
			+ "SELECT "
			+ "YEAR(creationtime), MONTH(creationtime), DAY(creationtime), COUNT(1) AS count "
			+ "FROM envelope "
			+ "WHERE YEAR(creationtime) = :year AND MONTH(creationtime) = :month "
			+ "GROUP BY YEAR(creationtime), MONTH(creationtime), DAY(creationtime)"
			+ ") AS counts";

	private static final String UNIQUE_TYPES_QUERY = "SELECT DISTINCT type FROM envelope WHERE creationtime BETWEEN :start AND :stop";

	private static final String COUNT_FOR_TYPE_QUERY = "SELECT COUNT(type) FROM envelope WHERE type = :type AND creationtime BETWEEN :start AND :stop";

	private static final String UNIQUE_USERS_QUERY = "SELECT DISTINCT senderidentity FROM envelope WHERE creationtime BETWEEN :start AND :stop";

	private static final String COUNT_FOR_USER_QUERY = "SELECT COUNT(senderidentity) FROM envelope WHERE senderidentity = :user AND creationtime BETWEEN :start AND :stop";

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public Table getEventsByTime(DateTime start, DateTime stop,
			TimeScale timeScale) throws Exception {
		Table table = null;
		if (timeScale == TimeScale.MONTH) {
			table = getEventsByTimeForMonthTimeScale(start, stop);
		} else {
			throw new UnsupportedOperationException(
					"Only handling MONTH time scales!");
		}

		logger.debug(table.toString());
		return table;
	}

	private Table getEventsByTimeForMonthTimeScale(DateTime start, DateTime stop) {
		start = start.withDayOfMonth(1);
		stop = stop.withDayOfMonth(stop.dayOfMonth().getMaximumValue());
		DateTime currentDateTime = new DateTime(start.getMillis());

		Table table = new Table();
		table.setColumnHeaders(Arrays.asList(new ColumnHeader("Date",
				ColumnType.DATE_YEAR_MONTH), new ColumnHeader("Minimum",
				ColumnType.LONG),
				new ColumnHeader("Average", ColumnType.DOUBLE),
				new ColumnHeader("Maximum", ColumnType.LONG), new ColumnHeader(
						"Sum", ColumnType.LONG), new ColumnHeader("Count",
						ColumnType.LONG)));

		while (currentDateTime.isBefore(stop)) {
			Object[] result = getEventsByTimeForMonth(currentDateTime);
			String formattedDateTime = formatDateTime(currentDateTime,
					ColumnType.DATE_YEAR_MONTH);
			Object[] objects = ArrayUtils.add(result, 0, formattedDateTime);

			Row row = new Row(objects);
			table.addRow(row);

			currentDateTime = currentDateTime.plusMonths(1);
		}

		return table;
	}

	private Object[] getEventsByTimeForMonth(DateTime dateTime) {
		Query query = entityManager
				.createNativeQuery(EVENTS_BY_TIME_FOR_MONTH_QUERY);
		query.setParameter("year", dateTime.getYear());
		query.setParameter("month", dateTime.getMonthOfYear());

		Object[] result = (Object[]) query.getSingleResult();
		return result;
	}

	private String formatDateTime(DateTime currentDateTime,
			ColumnType columnType) {
		return currentDateTime.toString(DateTimeFormat.forPattern(columnType
				.getFormat()));
	}

	@Transactional(readOnly = true)
	public Table getEventsByType(DateTime start, DateTime stop)
			throws Exception {
		Table table = new Table();
		table.setColumnHeaders(Arrays.asList(new ColumnHeader("Topic",
				ColumnType.STRING), new ColumnHeader("Count", ColumnType.LONG)));

		List<String> types = getTypes(start, stop);
		for (String type : types) {
			Object result = getCountForType(start, stop, type);
			Object[] objects = Arrays.asList(type, result).toArray();

			Row row = new Row(objects);
			table.addRow(row);
		}

		logger.debug(table.toString());
		return table;
	}

	@SuppressWarnings("unchecked")
	private List<String> getTypes(DateTime start, DateTime stop) {
		Query query = entityManager.createNativeQuery(UNIQUE_TYPES_QUERY);
		query.setParameter("start", start.toDate());
		query.setParameter("stop", stop.toDate());

		List<String> results = (List<String>) query.getResultList();
		return results;
	}

	private Object getCountForType(DateTime start, DateTime stop, String type) {
		Query query = entityManager.createNativeQuery(COUNT_FOR_TYPE_QUERY);
		query.setParameter("type", type);
		query.setParameter("start", start.toDate());
		query.setParameter("stop", stop.toDate());

		Object result = query.getSingleResult();
		return result;
	}

	@Transactional(readOnly = true)
	public Table getEventsByUser(DateTime start, DateTime stop)
			throws Exception {
		Table table = new Table();
		table.setColumnHeaders(Arrays.asList(new ColumnHeader("User",
				ColumnType.STRING), new ColumnHeader("Count", ColumnType.LONG)));

		List<String> users = getUsers(start, stop);
		for (String user : users) {
			Object result = getCountForUser(start, stop, user);
			Object[] objects = Arrays.asList(user, result).toArray();

			Row row = new Row(objects);
			table.addRow(row);
		}

		logger.debug(table.toString());
		return table;
	}

	@SuppressWarnings("unchecked")
	private List<String> getUsers(DateTime start, DateTime stop) {
		Query query = entityManager.createNativeQuery(UNIQUE_USERS_QUERY);
		query.setParameter("start", start.toDate());
		query.setParameter("stop", stop.toDate());

		List<String> results = (List<String>) query.getResultList();
		return results;
	}

	private Object getCountForUser(DateTime start, DateTime stop, String user) {
		Query query = entityManager.createNativeQuery(COUNT_FOR_USER_QUERY);
		query.setParameter("user", user);
		query.setParameter("start", start.toDate());
		query.setParameter("stop", stop.toDate());

		Object result = query.getSingleResult();
		return result;
	}
	
	
}
