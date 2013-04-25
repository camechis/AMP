package gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class BatchWriterFactoryImpl implements BatchWriterFactory,
		InitializingBean {
	private static final Logger logger = LoggerFactory
			.getLogger(BatchWriterFactoryImpl.class);

	private static final int DEFAULT_MAX_WRITE_THREADS = 10;

	private static final long DEFAULT_MAX_LATENCY = 1000L;

	private static final long DEFAULT_MAX_MEMORY = 1000000L;

	/**
	 * only one version of each Accumulo entry is required
	 */
	private static final String MAX_VERSIONS = "1";

	/**
	 * connects to an Accumulo instance
	 */
	private Connector connector;

	/**
	 * administers tables
	 */
	private TableOperations tableOperations;

	/**
	 * the name of the table to insert data into
	 */
	private String tableName;

	/**
	 * maximum memory to batch before writing (bytes)
	 */
	private long maxMemory = DEFAULT_MAX_MEMORY;

	/**
	 * allow the maximum time to hold a batch before writing (milliseconds)
	 */
	private long maxLatency = DEFAULT_MAX_LATENCY;

	/**
	 * the maximum number of threads to use for writing data to the tablet
	 * servers
	 */
	private int maxWriteThreads = DEFAULT_MAX_WRITE_THREADS;

	public BatchWriterFactoryImpl setConnector(Connector connector) {
		if (connector == null)
			throw new NullPointerException("connector is null!");

		this.connector = connector;
		return this;
	}

	public BatchWriterFactoryImpl setTableOperations(
			TableOperations tableOperations) {
		if (tableOperations == null)
			throw new NullPointerException("tableOperations is null!");

		this.tableOperations = tableOperations;
		return this;
	}

	public BatchWriterFactoryImpl setTableName(String tableName) {
		if (StringUtils.isBlank(tableName))
			throw new IllegalArgumentException("tableName is blank!");

		this.tableName = tableName;
		return this;
	}

	public BatchWriterFactoryImpl setMaxMemory(long maxMemory) {
		if (maxMemory < 0)
			throw new IllegalArgumentException("maxMemory is negative!");

		this.maxMemory = maxMemory;
		return this;
	}

	public BatchWriterFactoryImpl setMaxLatency(long maxLatency) {
		if (maxLatency < 0)
			throw new IllegalArgumentException("maxLatency is negative!");

		this.maxLatency = maxLatency;
		return this;
	}

	public BatchWriterFactoryImpl setMaxWriteThreads(int maxWriteThreads) {
		if (maxWriteThreads < 0)
			throw new IllegalArgumentException("maxWriteThreads is negative!");

		this.maxWriteThreads = maxWriteThreads;
		return this;
	}

	public BatchWriter createBatchWriter() {
		try {
			if (!tableOperations.exists(tableName)) {
				tableOperations.create(tableName);

				// set max versions
				tableOperations.setProperty(tableName,
						"table.iterator.scan.vers.opt.maxVersions",
						MAX_VERSIONS);
				tableOperations.setProperty(tableName,
						"table.iterator.minc.vers.opt.maxVersions",
						MAX_VERSIONS);
				tableOperations.setProperty(tableName,
						"table.iterator.majc.vers.opt.maxVersions",
						MAX_VERSIONS);
			}

			BatchWriter writer = connector.createBatchWriter(tableName,
					maxMemory, maxLatency, maxWriteThreads);
			return writer;
		} catch (Exception e) {
			logger.error("Unable to create batch writer!", e);
		}

		return null;
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("connector: " + connector);
		logger.info("tableOperations: " + tableOperations);
		logger.info("tableName: " + tableName);
		logger.info("maxMemory: " + maxMemory);
		logger.info("maxLatency: " + maxLatency);
		logger.info("maxWriteThreads: " + maxWriteThreads);
	}
}
