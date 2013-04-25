package gel.dao;

import cmf.bus.Envelope;

public interface DatastoreWriter {
	void initialize() throws Exception;

	void write(Envelope envelope) throws Exception;

	void close();
}
