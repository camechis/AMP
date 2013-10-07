package amp.gel.dao.impl.derby.data.events.search;

/**
 * This event is returned as a response to a {@link DocumentSearchRequestEvent}
 * and contains the documents requested.
 */
public class DocumentSearchResultEvent extends ResultEvent {

	private static final long serialVersionUID = -2555216895431477459L;

	/**
	 * Creates a new instance of the DocumentSearchResultEvent type. This type
	 * is usually not constructed by requesting processes.
	 */
	public DocumentSearchResultEvent() {
		super();
	}
}
