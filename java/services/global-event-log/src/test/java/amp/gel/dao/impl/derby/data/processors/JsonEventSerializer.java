package amp.gel.dao.impl.derby.data.processors;

public class JsonEventSerializer extends
		amp.messaging.JsonSerializationProcessor implements
		EventSequenceProcessor {

	public JsonEventSerializer() {
		
	}

	public void restartEventSequence() {
		// do nothing
	}
}
