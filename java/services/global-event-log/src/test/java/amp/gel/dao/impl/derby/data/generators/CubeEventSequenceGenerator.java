package amp.gel.dao.impl.derby.data.generators;

import java.util.ArrayList;
import java.util.List;

import amp.gel.dao.impl.derby.data.events.core.Event;
import amp.gel.dao.impl.derby.data.events.haruspex.CubeConfigRequestEvent;
import amp.gel.dao.impl.derby.data.events.haruspex.CubeConfigResponseEvent;
import amp.gel.dao.impl.derby.data.events.haruspex.CubeSearchEvent;
import amp.gel.dao.impl.derby.data.events.haruspex.CubeSearchResponseEvent;

public class CubeEventSequenceGenerator implements EventSequenceGenerator {

	public List<Event> generate() {
		List<Event> eventSequence = new ArrayList<Event>();

		eventSequence.add(new CubeConfigRequestEvent());
		eventSequence.add(new CubeConfigResponseEvent());
		eventSequence.add(new CubeSearchEvent());
		eventSequence.add(new CubeSearchResponseEvent());

		return eventSequence;
	}
}
