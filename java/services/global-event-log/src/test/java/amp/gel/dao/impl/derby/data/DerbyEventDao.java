package amp.gel.dao.impl.derby.data;

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

import amp.gel.domain.ColumnHeader;
import amp.gel.domain.ColumnType;
import amp.gel.domain.Row;
import amp.gel.domain.Table;
import amp.gel.domain.TimeScale;

public class DerbyEventDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyEventDao.class);

	private static final String EVENTS_BY_TIME_FOR_MONTH_QUERY = "SELECT "
			+ "MIN(count), AVG(CAST(count AS DOUBLE PRECISION)), MAX(count), SUM(count), COUNT(count) "
			+ "FROM ("
			+ "SELECT "
			+ "YEAR(creationtime), MONTH(creationtime), DAY(creationtime), COUNT(1) AS count "
			+ "FROM envelope "
			+ "WHERE YEAR(creationtime) = :year AND MONTH(creationtime) = :month "
			+ "GROUP BY YEAR(creationtime), MONTH(creationtime), DAY(creationtime)"
			+ ") AS counts";

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
}
