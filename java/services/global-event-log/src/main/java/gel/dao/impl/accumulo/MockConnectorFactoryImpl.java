package gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class MockConnectorFactoryImpl implements ConnectorFactory,
		InitializingBean {
	private static final Logger logger = LoggerFactory
			.getLogger(MockConnectorFactoryImpl.class);

	/**
	 * the name of specific accumulo instance
	 */
	protected String instanceName;

	/**
	 * a valid accumulo user
	 */
	protected String user;

	/**
	 * A UTF-8 encoded password
	 */
	protected String password;

	public MockConnectorFactoryImpl setInstanceName(String instanceName) {
		if (StringUtils.isBlank(instanceName))
			throw new IllegalArgumentException("instanceName is blank!");

		this.instanceName = instanceName;
		return this;
	}

	public MockConnectorFactoryImpl setUser(String user) {
		if (StringUtils.isBlank(user))
			throw new IllegalArgumentException("user is blank!");

		this.user = user;
		return this;
	}

	public MockConnectorFactoryImpl setPassword(String password) {
		if (password == null)
			throw new NullPointerException("password is null!");

		this.password = password;
		return this;
	}

	public Connector createConnector() {
		try {
			Connector connector = new MockInstance(instanceName).getConnector(
					user, password);
			return connector;
		} catch (Exception e) {
			logger.error("Unable to create connector!", e);
		}

		return null;
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("instanceName: " + instanceName);
		logger.info("user: " + user);
		logger.info("password: " + password);
	}
}
