package amp.gel.dao.impl.derby;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import amp.gel.dao.DatastoreWriter;

public class DerbyWriter implements DatastoreWriter {
	private static final Logger logger = LoggerFactory
			.getLogger(DerbyWriter.class);

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void initialize() throws Exception {
		// do nothing
		logger.info("Initialized DerbyWriter.");
	}

	@Transactional(readOnly = false)
	public void write(cmf.bus.Envelope envelope) throws Exception {
		entityManager.persist(new Envelope(envelope));
	}

	@Transactional(readOnly = false)
	public void close() {
		entityManager.flush();

		logger.info("Closed DerbyWriter.");
	}
}
