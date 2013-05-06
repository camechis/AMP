package amp.gel.health;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.HealthCheck;

public class DerbyHealthCheck extends HealthCheck {
	private static final String DEFAULT_VALIDATION_QUERY = "SELECT 1 FROM SYSIBM.SYSDUMMY1";

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyHealthCheck.class);

	private DataSource dataSource;

	private String validationQuery = DEFAULT_VALIDATION_QUERY;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public DerbyHealthCheck() {
		super("derby");
	}

	@Override
	protected Result check() throws Exception {
		Result health = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(validationQuery);

			health = Result.healthy();
			logger.info("Apache Derby is healthy.");
		} catch (Exception e) {
			health = Result.unhealthy(e);
			logger.error("Unable to perform health check!", e);
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				// do nothing
			}
			try {
				statement.close();
			} catch (Exception e) {
				// do nothing
			}
			try {
				connection.close();
			} catch (Exception e) {
				// do nothing
			}
		}

		return health;
	}
}
