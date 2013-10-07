package amp.gel.dao.impl.derby;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import amp.gel.dao.DatastoreTestHelper;

public class DerbyTestHelper implements DatastoreTestHelper {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public long getRowCount(String tableName) throws Exception {
		Query query = entityManager
				.createNativeQuery("SELECT COUNT(*) FROM envelope");
		return (Integer) query.getSingleResult();
	}
}
