package amp.gel.dao.impl.derby.data.processors;

import amp.utility.serialization.GsonSerializer;
import amp.utility.serialization.ISerializer;

public class JsonEventSerializer extends
		amp.eventing.serializers.JsonEventSerializer implements
		EventSequenceProcessor {

	public JsonEventSerializer() {
		this(new GsonSerializer());
	}

	public JsonEventSerializer(ISerializer serializer) {
		super(serializer);
	}

	public void restartEventSequence() {
		// do nothing
	}
}
