package amp.gel.dao.impl.derby.data.generators;

import java.util.Arrays;
import java.util.List;

import amp.gel.dao.impl.derby.data.events.core.Event;
import amp.gel.dao.impl.derby.data.events.core.ExportEvent;

public class ExportEventSequenceGenerator implements EventSequenceGenerator {

	public List<Event> generate() {
		ExportEvent exportEvent = new ExportEvent();
		return Arrays.asList((Event) exportEvent);
	}
}
