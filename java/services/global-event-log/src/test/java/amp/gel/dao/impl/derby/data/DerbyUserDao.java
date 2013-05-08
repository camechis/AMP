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

public class DerbyUserDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyUserDao.class);

	private static final String UNIQUE_USERS_QUERY = "SELECT DISTINCT senderidentity FROM envelope WHERE creationtime BETWEEN :start AND :stop";

	private static final String COUNT_FOR_USER_QUERY = "SELECT COUNT(senderidentity) FROM envelope WHERE senderidentity = :user AND creationtime BETWEEN :start AND :stop";

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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
