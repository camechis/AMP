package amp.gel.dao.impl.accumulo;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.junit.Before;
import org.junit.Test;

import amp.gel.dao.impl.accumulo.BatchWriterFactoryImpl;

public class BatchWriterFactoryImplTest {

	private static final String TABLE_NAME = "TEST_TABLE";

	private BatchWriterFactoryImpl batchWriterFactory = new BatchWriterFactoryImpl();

	private Connector connector = mock(Connector.class);

	private TableOperations tableOperations = mock(TableOperations.class);

	@Before
	public void setUp() {
		batchWriterFactory.setConnector(connector);
		batchWriterFactory.setTableOperations(tableOperations);
		batchWriterFactory.setTableName(TABLE_NAME);
	}

	@Test
	public void testCreateTableIfNotExists() {
		when(tableOperations.exists(TABLE_NAME)).thenReturn(false);

		batchWriterFactory.createBatchWriter();

		try {
			verify(tableOperations).create(TABLE_NAME);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testDoNotCreateTableIfExists() {
		when(tableOperations.exists(TABLE_NAME)).thenReturn(true);

		batchWriterFactory.createBatchWriter();

		try {
			verify(tableOperations, never()).create(TABLE_NAME);
		} catch (Exception e) {
			fail();
		}
	}
}
