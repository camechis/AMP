package amp.gel.dao.impl.derby;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import amp.gel.dao.DatastoreWriter;

public class DerbyWriter implements DatastoreWriter {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void initialize() throws Exception {
		// do nothing
	}

	@Transactional(readOnly = false)
	public void write(cmf.bus.Envelope envelope) throws Exception {
		entityManager.persist(new Envelope(envelope));
	}

	public void close() {
		entityManager.flush();
	}
}
