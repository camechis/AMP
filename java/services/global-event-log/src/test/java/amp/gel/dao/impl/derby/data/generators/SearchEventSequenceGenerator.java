package amp.gel.dao.impl.derby.data.generators;

import java.util.ArrayList;
import java.util.List;

import amp.gel.dao.impl.derby.data.events.core.Event;
import amp.gel.dao.impl.derby.data.events.search.DocumentRequestEvent;
import amp.gel.dao.impl.derby.data.events.search.DocumentResultEvent;
import amp.gel.dao.impl.derby.data.events.search.DocumentSearchRequestEvent;
import amp.gel.dao.impl.derby.data.events.search.DocumentSearchResultEvent;
import amp.gel.dao.impl.derby.data.events.search.FacetSearchResultEvent;
import amp.gel.dao.impl.derby.data.events.search.SetLikesEvent;
import amp.gel.dao.impl.derby.data.events.search.SimilarDocumentsResultEvent;

public class SearchEventSequenceGenerator implements EventSequenceGenerator {

	public List<Event> generate() {
		List<Event> eventSequence = new ArrayList<Event>();

		eventSequence.add(new DocumentSearchRequestEvent());
		eventSequence.add(new DocumentSearchResultEvent());
		eventSequence.add(new FacetSearchResultEvent());
		eventSequence.add(new DocumentRequestEvent());
		eventSequence.add(new DocumentResultEvent());
		eventSequence.add(new SetLikesEvent());
		eventSequence.add(new SimilarDocumentsResultEvent());

		return eventSequence;
	}
}
