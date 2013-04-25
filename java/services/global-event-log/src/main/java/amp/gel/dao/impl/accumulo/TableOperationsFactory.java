package amp.gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.admin.TableOperations;

public interface TableOperationsFactory {
	TableOperations createTableOperations();
}
