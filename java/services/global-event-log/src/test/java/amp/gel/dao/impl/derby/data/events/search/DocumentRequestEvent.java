package amp.gel.dao.impl.derby.data.events.search;

/**
 * This event is used to initiate a request for a single document. The request
 * may specify if additional similar documents should also be returned as
 * "recommendations." This event is generally intended to be used as an RPC
 * event to which the handling service will respond with a
 * {@link DocumentResultEvent}.
 */
public class DocumentRequestEvent extends SearchEvent {

	/**
	 * This is the default number of similar documents that will be returned as
	 * recommendations.
	 */
	public static final int DEFAULT_MLT_DOC_COUNT = 5;

	private static final long serialVersionUID = -7803102806524430034L;

	private String documentId;

	/**
	 * When returning a response for the document, include "More Like This" with
	 * the response instead of as a separate event.
	 */
	private boolean includeMoreLikeThis = false;

	/**
	 * Override the default count for "More Like This" results with the
	 * client-requested value.
	 */
	private int moreLikeThisCount = DEFAULT_MLT_DOC_COUNT;

	/**
	 * Creates a new instance of the DocumentRequestEvent.
	 */
	public DocumentRequestEvent() {
		super();
	}

	/**
	 * Returns the document ID of the document being requested.
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Specifies the document ID of the document being requested.
	 * 
	 * @param documentId
	 *            The document ID.
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * Indicates if additional similar documents should returned as
	 * recommendations. The default value is false.
	 * 
	 * @return True of recommendations should be returned, otherwise false.
	 */
	public boolean includeMoreLikeThis() {
		return includeMoreLikeThis;
	}

	/**
	 * Specifies if additional similar documents should returned as
	 * recommendations. The default value is false.
	 * 
	 * @param includeMoreLikeThis
	 *            Set to true to return recommendations, false otherwise.
	 */
	public void setIncludeMoreLikeThis(boolean includeMoreLikeThis) {
		this.includeMoreLikeThis = includeMoreLikeThis;
	}

	/**
	 * Returns the number of additional documents to return if
	 * {@link #includeMoreLikeThis()} is true.
	 */
	public int getMoreLikeThisCount() {
		return moreLikeThisCount;
	}

	/**
	 * Specifies the number of additional documents to return if
	 * {@link #includeMoreLikeThis()} is true
	 * 
	 * @param moreLikeThisCount
	 *            The number of additional documents.
	 */
	public void setMoreLikeThisCount(int moreLikeThisCount) {
		this.moreLikeThisCount = moreLikeThisCount;
	}
}