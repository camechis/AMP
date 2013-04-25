package gel.dao.impl.accumulo;

import org.apache.accumulo.core.client.Connector;

public interface ConnectorFactory {
	Connector createConnector();
}