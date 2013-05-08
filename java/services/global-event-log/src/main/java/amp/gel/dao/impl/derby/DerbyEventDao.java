package amp.gel.dao.impl.derby;

import static amp.gel.domain.ColumnHeader.AVG;
import static amp.gel.domain.ColumnHeader.COUNT;
import static amp.gel.domain.ColumnHeader.MAX;
import static amp.gel.domain.ColumnHeader.MIN;
import static amp.gel.domain.ColumnHeader.SUM;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import amp.gel.dao.EventDao;
import amp.gel.domain.ColumnHeader;
import amp.gel.domain.ColumnType;
import amp.gel.domain.Row;
import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public class DerbyEventDao implements EventDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyEventDao.class);

	private static final String EVENTS_BY_TIME_FOR_MONTH_QUERY = "SELECT "
			+ "MIN(count), AVG(CAST(count AS DOUBLE PRECISION)), MAX(count), SUM(count), COUNT(count) "
			+ "FROM "
			+ "("
			+ "SELECT COUNT(1) AS count "
			+ "FROM envelope "
			+ "WHERE YEAR(creationtime) = :year AND MONTH(creationtime) = :month "
			+ "GROUP BY YEAR(creationtime), MONTH(creationtime), DAY(creationtime)"
			+ ") AS counts";

	private static final String EVENTS_BY_TIME_FOR_DAY_QUERY = "SELECT "
			+ "MIN(count), AVG(CAST(count AS DOUBLE PRECISION)), MAX(count), SUM(count), COUNT(count) "
			+ "FROM "
			+ "("
			+ "SELECT COUNT(1) AS count "
			+ "FROM envelope "
			+ "WHERE YEAR(creationtime) = :year AND MONTH(creationtime) = :month AND DAY(creationtime) = :day "
			+ "GROUP BY YEAR(creationtime), MONTH(creationtime), DAY(creationtime), HOUR(creationtime)"
			+ ") AS counts";

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * amp.gel.dao.impl.derby.EventDao#getEventsByTime(org.joda.time.DateTime,
	 * org.joda.time.DateTime, amp.gel.domain.TimeScale)
	 */
	@Transactional(readOnly = true)
	public Table getEventsByTime(DateTime start, DateTime stop,
			TimeScale timeScale) throws Exception {
		Table table = null;
		if (timeScale == TimeScale.MONTH) {
			table = getEventsByTimeForMonthTimeScale(start, stop);
		} else if (timeScale == TimeScale.DAY) {
			table = getEventsByTimeForDayTimeScale(start, stop);
		} else {
			// TODO: handle other time scales
			throw new UnsupportedOperationException(
					"Only handling MONTH time scales!");
		}

		logger.debug(table.toString());
		return table;
	}

	private Table getEventsByTimeForMonthTimeScale(DateTime start, DateTime stop) {
		// start from first day of the month
		start = start.withDayOfMonth(1);
		// end with the last day of the month
		stop = stop.withDayOfMonth(stop.dayOfMonth().getMaximumValue());
		DateTime currentDateTime = new DateTime(start.getMillis());

		ColumnType dateColumnType = ColumnType.DATE_YEAR_MONTH;

		Table table = new Table();
		table.setColumnHeaders(Arrays.asList(new ColumnHeader("Date",
				dateColumnType), MIN, AVG, MAX, SUM, COUNT));

		while (currentDateTime.isBefore(stop)) {
			Object[] result = getEventsByTimeForMonth(currentDateTime);
			String formattedDateTime = formatDateTime(currentDateTime,
					dateColumnType);
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

	private Table getEventsByTimeForDayTimeScale(DateTime start, DateTime stop) {
		// start from first hour of the day
		start = start.withTimeAtStartOfDay();
		// end with the last hour of the day
		stop = stop.withTime(23, 59, 59, 999);
		DateTime currentDateTime = new DateTime(start.getMillis());

		ColumnType dateColumnType = ColumnType.DATE_YEAR_MONTH_DAY;

		Table table = new Table();
		table.setColumnHeaders(Arrays.asList(new ColumnHeader("Date",
				dateColumnType), MIN, AVG, MAX, SUM, COUNT));

		while (currentDateTime.isBefore(stop)) {
			Object[] result = getEventsByTimeForDay(currentDateTime);
			String formattedDateTime = formatDateTime(currentDateTime,
					dateColumnType);
			Object[] objects = ArrayUtils.add(result, 0, formattedDateTime);

			Row row = new Row(objects);
			table.addRow(row);

			currentDateTime = currentDateTime.plusDays(1);
		}

		return table;
	}

	private Object[] getEventsByTimeForDay(DateTime dateTime) {
		Query query = entityManager
				.createNativeQuery(EVENTS_BY_TIME_FOR_DAY_QUERY);
		query.setParameter("year", dateTime.getYear());
		query.setParameter("month", dateTime.getMonthOfYear());
		query.setParameter("day", dateTime.getDayOfMonth());

		Object[] result = (Object[]) query.getSingleResult();
		return result;
	}

	private String formatDateTime(DateTime currentDateTime,
			ColumnType columnType) {
		return currentDateTime.toString(DateTimeFormat.forPattern(columnType
				.getFormat()));
	}

	public Table getEventsByTimeForUser(DateTime start, DateTime stop,
			TimeScale timeScale, String user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Table getEventsByTimeForType(DateTime start, DateTime stop,
			TimeScale timeScale, String type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
