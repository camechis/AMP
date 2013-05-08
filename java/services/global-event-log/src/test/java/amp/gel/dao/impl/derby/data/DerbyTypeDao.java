package amp.gel.dao.impl.derby.data;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import amp.gel.domain.ColumnHeader;
import amp.gel.domain.ColumnType;
import amp.gel.domain.Row;
import amp.gel.domain.Table;

public class DerbyTypeDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyTypeDao.class);

	private static final String UNIQUE_TYPES_QUERY = "SELECT DISTINCT type FROM envelope WHERE creationtime BETWEEN :start AND :stop";

	private static final String COUNT_FOR_TYPE_QUERY = "SELECT COUNT(type) FROM envelope WHERE type = :type AND creationtime BETWEEN :start AND :stop";

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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
}
