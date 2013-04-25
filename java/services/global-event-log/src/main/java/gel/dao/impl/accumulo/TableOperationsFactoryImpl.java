package gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class TableOperationsFactoryImpl implements TableOperationsFactory,
		InitializingBean {
	private static final Logger logger = LoggerFactory
			.getLogger(TableOperationsFactoryImpl.class);

	/**
	 * connects to an Accumulo instance
	 */
	private Connector connector;

	public TableOperationsFactoryImpl setConnector(Connector connector) {
		if (connector == null)
			throw new NullPointerException("connector is null!");

		this.connector = connector;
		return this;
	}

	public TableOperations createTableOperations() {
		TableOperations tableOps = connector.tableOperations();
		return tableOps;
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("connector: " + connector);
	}
}
