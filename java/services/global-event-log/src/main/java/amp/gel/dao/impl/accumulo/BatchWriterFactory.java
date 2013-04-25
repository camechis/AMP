package amp.gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.BatchWriter;

public interface BatchWriterFactory {
	BatchWriter createBatchWriter();
}
