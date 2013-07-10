package amp.gel.dao.impl.derby;

import static amp.gel.domain.ColumnHeader.COUNT;
import static amp.gel.domain.ColumnHeader.TOPIC;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import amp.gel.dao.TypeDao;
import amp.gel.domain.Row;
import amp.gel.domain.Table;

public class DerbyTypeDao implements TypeDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyTypeDao.class);

	private static final String UNIQUE_TYPES_QUERY = "SELECT DISTINCT type FROM envelope WHERE creationtime BETWEEN :start AND :stop";

	private static final String COUNT_FOR_TYPE_QUERY = "SELECT COUNT(type) FROM envelope WHERE type = :type AND creationtime BETWEEN :start AND :stop";

	private static final String USER_CONDITIONAL = " AND senderidentity = :user";

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * amp.gel.dao.impl.derby.TypeDao#getEventsByType(org.joda.time.DateTime,
	 * org.joda.time.DateTime)
	 */
	@Transactional(readOnly = true)
	public Table getEventsByType(DateTime start, DateTime stop)
			throws Exception {
		Table table = getEventsByTypeForUser(start, stop, null);
		return table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * amp.gel.dao.impl.derby.TypeDao#getEventsByTypeForUser(org.joda.time.DateTime
	 * , org.joda.time.DateTime, java.lang.String)
	 */
	@Transactional(readOnly = true)
	public Table getEventsByTypeForUser(DateTime start, DateTime stop,
			String user) throws Exception {
		Table table = new Table();
		table.setColumnHeaders(Arrays.asList(TOPIC, COUNT));

		List<String> types = getTypes(start, stop, user);
		for (String type : types) {
			Object result = getCount(start, stop, type, user);
			Object[] objects = Arrays.asList(type, result).toArray();

			Row row = new Row(objects);
			table.addRow(row);
		}

		logger.debug(table.toString());
		return table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see amp.gel.dao.impl.derby.TypeDao#getTypes(org.joda.time.DateTime,
	 * org.joda.time.DateTime)
	 */
	@Transactional(readOnly = true)
	public List<String> getTypes(DateTime start, DateTime stop) {
		List<String> types = getTypes(start, stop, null);
		logger.debug(types.toString());
		return types;
	}

	@SuppressWarnings("unchecked")
	private List<String> getTypes(DateTime start, DateTime stop, String user) {
		String sql = UNIQUE_TYPES_QUERY;
		if (StringUtils.isNotBlank(user)) {
			sql += USER_CONDITIONAL;
		}

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("start", start.toDate());
		query.setParameter("stop", stop.toDate());

		if (StringUtils.isNotBlank(user)) {
			query.setParameter("user", user);
		}

		List<String> results = (List<String>) query.getResultList();
		return results;
	}

	private Object getCount(DateTime start, DateTime stop, String type,
			String user) {

		String sql = COUNT_FOR_TYPE_QUERY;
		if (StringUtils.isNotBlank(user)) {
			sql += USER_CONDITIONAL;
		}

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("type", type);
		query.setParameter("start", start.toDate());
		query.setParameter("stop", stop.toDate());

		if (StringUtils.isNotBlank(user)) {
			query.setParameter("user", user);
		}

		Object result = query.getSingleResult();
		return result;
	}
}
