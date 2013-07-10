package amp.gel.dao.impl.derby.data.events.search;

/**
 * This event is returned as a response to a {@link DocumentSearchRequestEvent}
 * if facet data was requested and contains the requested facet data.
 */
public class FacetSearchResultEvent extends ResultEvent {

	private static final long serialVersionUID = -6318983322326642021L;

	/**
	 * Creates a new instance of the FacetSearchResultEvent type. This type is
	 * usually not constructed by requesting processes.
	 */
	public FacetSearchResultEvent() {
		super();
	}
}
