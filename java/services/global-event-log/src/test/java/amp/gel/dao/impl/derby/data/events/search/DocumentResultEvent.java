package amp.gel.dao.impl.derby.data.events.search;

/**
 * This event is return as the response to a {@link DocumentRequestEvent} and
 * contains the document requested as well as recommended similar documents if
 * also requested.
 */
public class DocumentResultEvent extends ResultEvent {

	private static final long serialVersionUID = -3375460055125001342L;

	/**
	 * Creates a new instance of the DocumentResultEvent type. This type is
	 * usually not constructed by requesting processes.
	 */
	public DocumentResultEvent() {
		super();
	}

	/**
	 * Creates a new instance of the DocumentResultEvent type and specifies the
	 * original {@link SearchEvent} that the new event is in response to. This
	 * type is usually not constructed by requesting processes.
	 */
	public DocumentResultEvent(SearchEvent se) {
		super();
		setSearchEvent(se);
	}
}