package gel.dao.impl.accumulo;

import gel.dao.DatastoreWriter;

import java.util.List;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.data.Mutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import cmf.bus.Envelope;

public class AccumuloWriter implements DatastoreWriter, InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(AccumuloWriter.class);

	/**
	 * send mutations to a single table in Accumulo
	 */
	private BatchWriter batchWriter;

	/**
	 * generates mutations from envelopes
	 */
	private MutationsGenerator mutationsGenerator;

	public AccumuloWriter setBatchWriter(BatchWriter batchWriter) {
		if (batchWriter == null)
			throw new NullPointerException("batchWriter is null!");

		this.batchWriter = batchWriter;
		return this;
	}

	public AccumuloWriter setMutationsGenerator(
			MutationsGenerator mutationsGenerator) {
		if (mutationsGenerator == null)
			throw new NullPointerException("mutationsGenerator is null!");

		this.mutationsGenerator = mutationsGenerator;
		return this;
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("batchWriter: " + batchWriter);
		logger.info("mutationsGenerator: " + mutationsGenerator);
	}

	public void initialize() throws Exception {
		// do nothing
	}

	public void write(Envelope envelope) throws Exception {
		List<Mutation> mutations = mutationsGenerator.generate(envelope);
		batchWriter.addMutations(mutations);
	}

	public void close() {
		if (batchWriter != null) {
			try {
				batchWriter.close();
			} catch (MutationsRejectedException e) {
				logger.error("Unable to close batch writer!", e);
			}
			batchWriter = null;
		}
	}
}