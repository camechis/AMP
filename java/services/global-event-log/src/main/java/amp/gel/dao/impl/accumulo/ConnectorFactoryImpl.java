package amp.gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class ConnectorFactoryImpl implements ConnectorFactory, InitializingBean {
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectorFactoryImpl.class);

	/**
	 * the name of specific accumulo instance
	 */
	protected String instanceName;

	/**
	 * a comma separated list of zoo keeper server locations. Each location can
	 * contain an optional port, of the format host:port.
	 */
	protected String zooKeepers;

	/**
	 * a valid accumulo user
	 */
	protected String user;

	/**
	 * A UTF-8 encoded password
	 */
	protected String password;

	public ConnectorFactoryImpl setInstanceName(String instanceName) {
		if (StringUtils.isBlank(instanceName))
			throw new IllegalArgumentException("instanceName is blank!");

		this.instanceName = instanceName;
		return this;
	}

	public ConnectorFactoryImpl setZooKeepers(String zooKeepers) {
		if (StringUtils.isBlank(zooKeepers))
			throw new IllegalArgumentException("zooKeepers is blank!");

		this.zooKeepers = zooKeepers;
		return this;
	}

	public ConnectorFactoryImpl setUser(String user) {
		if (StringUtils.isBlank(user))
			throw new IllegalArgumentException("user is blank!");

		this.user = user;
		return this;
	}

	public ConnectorFactoryImpl setPassword(String password) {
		if (password == null)
			throw new NullPointerException("password is null!");

		this.password = password;
		return this;
	}

	public Connector createConnector() {
		try {
			Connector connector = new ZooKeeperInstance(instanceName,
					zooKeepers).getConnector(user, password);
			return connector;
		} catch (Exception e) {
			logger.error("Unable to create connector!", e);
		}

		return null;
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("instanceName: " + instanceName);
		logger.info("zooKeepers: " + zooKeepers);
		logger.info("user: " + user);
		logger.info("password: " + password);
	}
}
