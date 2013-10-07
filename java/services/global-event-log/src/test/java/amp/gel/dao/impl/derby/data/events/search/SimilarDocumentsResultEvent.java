package amp.gel.dao.impl.derby.data.events.search;

/**
 * This event may be return in conjunction with the {@link ResultEvent} types
 * that return documents in order to recommend other similar documents. In which
 * case the process making the initial request must be write so as to expect
 * multiple response events of different types.
 * 
 * Certain {@link ResultEvent} such as the {@link DocumentResultEvent} contain
 * data structures that give them the the ability to return similar documents
 * without the use of this event type. It the case of services that return such
 * events it is up to the service to decide if it will use those data structures
 * or this event. For example, if selecting similar documents is a
 * time-intensive operation, the service may elect to return the original result
 * immediately with no similar documents and then return the similar documents
 * using an instance of this event those documents are available.
 */
public class SimilarDocumentsResultEvent extends ResultEvent {

	private static final long serialVersionUID = -311443774758858581L;

	/**
	 * Creates a new instance of the SimilarDocumentsResultEvent type.
	 */
	public SimilarDocumentsResultEvent() {
		super();
	}

	/**
	 * Creates a new instance of the SimilarDocumentsResultEvent type specifying
	 * the {@link SearchEvent} to which it is responding.
	 */
	public SimilarDocumentsResultEvent(SearchEvent se) {
		super();
		setSearchEvent(se);
	}
}