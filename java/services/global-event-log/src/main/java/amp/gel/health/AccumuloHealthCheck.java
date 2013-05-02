package amp.gel.health;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import amp.gel.dao.impl.accumulo.ConnectorFactory;
import amp.gel.dao.impl.accumulo.ConnectorFactoryImpl;

import com.yammer.metrics.core.HealthCheck;

public class AccumuloHealthCheck extends HealthCheck implements
		ApplicationContextAware {
	private static final Logger logger = LoggerFactory
			.getLogger(AccumuloHealthCheck.class);

	private ApplicationContext applicationContext;

	public AccumuloHealthCheck() {
		super("accumulo");
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Result check() throws Exception {
		Result health = null;

		try {
			ConnectorFactory connectorFactory = applicationContext.getBean(
					"connectorFactory", ConnectorFactoryImpl.class);
			Connector connector = connectorFactory.createConnector();
			Instance instance = connector.getInstance();

			String instanceName = instance.getInstanceName();
			logger.info("instanceName: " + instanceName);
			boolean healthy = StringUtils.isNotBlank(instanceName);
			health = (healthy) ? Result.healthy() : Result
					.unhealthy("instanceName is blank!");
		} catch (Exception e) {
			logger.error("Unable to perform health check!", e);
			health = Result.unhealthy(e);
		}

		return health;
	}
}
