package amp.gel.service;

import java.util.Map.Entry;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;

public class AccumuloTestHelper {
	public static final int DEFAULT_BATCH_SIZE = 20;

	/**
	 * connects to an Accumulo instance
	 */
	private Connector connector;

	/**
	 * administers tables
	 */
	private TableOperations tableOperations;

	public AccumuloTestHelper setConnector(Connector connector) {
		if (connector == null)
			throw new NullPointerException("connector is null!");

		this.connector = connector;
		return this;
	}

	public AccumuloTestHelper setTableOperations(TableOperations tableOperations) {
		if (tableOperations == null)
			throw new NullPointerException("tableOperations is null!");

		this.tableOperations = tableOperations;
		return this;
	}

	/**
	 * FIXME: Really inefficient implementation that is only suitable for
	 * testing.
	 */
	public long getEntryCount(String tableName) throws TableNotFoundException {
		long count = 0;
		Scanner scanner = connector.createScanner(tableName,
				new Authorizations());
		scanner.setBatchSize(DEFAULT_BATCH_SIZE);
		for (Entry<Key, Value> entry : scanner) {
			count++;
		}

		return count;
	}

	public void delete(String tableName) throws AccumuloException,
			AccumuloSecurityException, TableNotFoundException {
		tableOperations.delete(tableName);
	}
}
