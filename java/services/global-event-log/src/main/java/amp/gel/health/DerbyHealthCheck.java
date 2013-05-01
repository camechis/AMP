package amp.gel.health;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.HealthCheck;

public class DerbyHealthCheck extends HealthCheck {
	private static final String VALIDATION_QUERY = "SELECT 1 FROM SYSIBM.SYSDUMMY1";

	private static final Logger logger = LoggerFactory
			.getLogger(DerbyHealthCheck.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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
			resultSet = statement.executeQuery(VALIDATION_QUERY);
			health = Result.healthy();
		} catch (Exception e) {
			logger.error("Unable to perform health check!", e);
			health = Result.unhealthy(e);
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
