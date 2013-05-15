package amp.gel.dao.impl.accumulo;

import java.util.Map.Entry;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.user.WholeRowIterator;
import org.apache.accumulo.core.security.Authorizations;

import amp.gel.dao.DatastoreTestHelper;

public class AccumuloTestHelper implements DatastoreTestHelper {
	public static final int DEFAULT_BATCH_SIZE = 30;

	/**
	 * connects to an Accumulo instance
	 */
	private Connector connector;

	public DatastoreTestHelper setConnector(Connector connector) {
		if (connector == null)
			throw new NullPointerException("connector is null!");

		this.connector = connector;
		return this;
	}

	/**
	 * FIXME: Really inefficient implementation that is only suitable for
	 * testing.
	 */
	public long getRowCount(String tableName) throws TableNotFoundException {

		long count = 0; // since we are counting natual numbers, start with 1
		Scanner scanner = connector.createScanner(tableName,
				new Authorizations());
		scanner.setBatchSize(DEFAULT_BATCH_SIZE);
		scanner.addScanIterator(new IteratorSetting(Integer.MAX_VALUE, "row",
				WholeRowIterator.class));
		for (Entry<Key, Value> entry : scanner) {
			count++;
		}

		return count;
	}
}
