package amp.gel.health;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.HealthCheck;

public class AccumuloHealthCheck extends HealthCheck {
	private static final Logger logger = LoggerFactory
			.getLogger(AccumuloHealthCheck.class);

	/**
	 * connects to an Accumulo instance
	 */
	private Connector connector;

	/**
	 * the name of specific accumulo instance
	 */
	protected String instanceName;

	public AccumuloHealthCheck setConnector(Connector connector) {
		if (connector == null)
			throw new NullPointerException("connector is null!");

		this.connector = connector;
		return this;
	}

	public AccumuloHealthCheck setInstanceName(String instanceName) {
		if (StringUtils.isBlank(instanceName))
			throw new IllegalArgumentException("instanceName is blank!");

		this.instanceName = instanceName;
		return this;
	}

	public AccumuloHealthCheck() {
		super("accumulo");
	}

	@Override
	protected Result check() throws Exception {
		Result health = null;

		try {
			Instance instance = connector.getInstance();
			String instanceName = instance.getInstanceName();
			logger.info("instanceName: " + instanceName);

			boolean healthy = StringUtils.isNotBlank(instanceName)
					&& this.instanceName.equals(instanceName);
			health = (healthy) ? Result.healthy() : Result
					.unhealthy("instanceName is NOT valid!");
		} catch (Exception e) {
			logger.error("Unable to perform health check!", e);
			health = Result.unhealthy(e);
		}

		return health;
	}
}
