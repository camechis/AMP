package amp.gel.dao.impl.derby.data.processors;

import amp.eventing.ISerializer;

public class JsonEventSerializer extends
		amp.eventing.serializers.JsonEventSerializer implements
		EventSequenceProcessor {

	public JsonEventSerializer() {
		this(new amp.eventing.GsonSerializer());
	}

	public JsonEventSerializer(ISerializer serializer) {
		super(serializer);
	}

	public void restartEventSequence() {
		// do nothing
	}
}
