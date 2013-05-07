package amp.gel.dao.impl.derby.data.generators;

import java.util.List;

import amp.gel.dao.impl.derby.data.events.core.Event;

public interface EventSequenceGenerator {
	List<Event> generate();
}
