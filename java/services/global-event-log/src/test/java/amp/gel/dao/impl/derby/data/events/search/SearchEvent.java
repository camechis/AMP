package amp.gel.dao.impl.derby.data.events.search;

import org.hibernate.Criteria;

import amp.gel.dao.impl.derby.data.events.core.ClickstreamEvent;

/**
 * SearchEvent is the base event type for all events that in some form or
 * another request information and for which another event, presumably a
 * subclass of {@link ResultEvent} is expected in response.
 * 
 * Theoretically, due to the inclusion of a generic {@link Criteria} property,
 * it is possible that certain services could reply to instance of
 * {@link SearchEvent} itself verses relying on a more specific subclass,
 * however presently no such services exist and {@link SearchEvent} is only used
 * as a super class for other search types.
 * 
 * SearchEvent is subclass of {@link ClickstreamEvent} that specifies
 * {@link Action#SEARCH} as its action.
 */
public class SearchEvent extends ClickstreamEvent {

	private static final long serialVersionUID = -6050180444723589343L;
}
