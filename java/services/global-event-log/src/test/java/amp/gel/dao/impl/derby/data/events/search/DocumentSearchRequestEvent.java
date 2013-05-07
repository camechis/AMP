package amp.gel.dao.impl.derby.data.events.search;

/**
 * This event is used to initiate a document search. This event is generally
 * intended to be used as an RPC event which may receive multiple responses of
 * type {@link DocumentSearchResultEvent} and/or {@link FacetSearchResultEvent}
 */
public class DocumentSearchRequestEvent extends SearchEvent {

	private static final long serialVersionUID = 7133210058693514860L;

	/*
	 * Simple plain text query as entered by user Ex: "China"
	 */
	private String queryText;

	/*
	 * Row to start searching at, default is 0
	 */
	private Integer start = 0;

	/*
	 * Number of rows to fetch with a default
	 */
	private Integer rows = 10;

	private String sortField;

	private SortDirection sortDirection = SortDirection.DESC;

	/**
	 * Enumeration that defines the values that may be used with
	 * {@link DocumentSearchRequestEvent#setSortDirection(SortDirection)} or
	 * {@link DocumentSearchRequestEvent#getSortDirection()}
	 */
	public enum SortDirection {
		/**
		 * Indicates that results should be sorted in ascending order.
		 */
		ASC,

		/**
		 * Indicates that results should be sorted in descending order.
		 */
		DESC;
	}

	/**
	 * Creates an new instance of a {@link DocumentSearchResultEvent}.
	 */
	public DocumentSearchRequestEvent() {
		super();
	}

	/**
	 * \ * Returns the plain text query entered by the user. Ex: "China"
	 */
	public String getQueryText() {
		return queryText;
	}

	/**
	 * Specifies the simple plain text query as entered by user Ex: "China"
	 * 
	 * @param queryText
	 *            The plain text query.
	 */
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	/**
	 * Indicates the zero-based ordinal of the first row from the result set
	 * that should be returned where the result set is the full set of documents
	 * matching the search criteria ordered as specified by
	 * {@link #getSortField()} and {@link #getSortDirection()}.
	 * 
	 * @return An integer indicating the zero-based ordinal of the first row to
	 *         return.
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * Specifies the zero-based ordinal value indicating the first row from the
	 * result set that should be returned where the result set is the full set
	 * of documents matching the search criteria ordered as specified by {link
	 * {@link #getSortField()} and {@link #getSortDirection()}. Can be used in
	 * conjunction with {@link #setRows(Integer)} to return portions of the
	 * total result set in a "paged" manner.
	 * 
	 * @param start
	 *            An integer indicating the zero-based ordinal of the first row
	 *            to return.
	 */
	public void setStart(Integer start) {
		this.start = start;
	}

	/**
	 * Indicates the maximum number of rows from the result set, starting with
	 * the row indicated by {@link #getStart()} that should be returned. Fewer
	 * rows may be returned if the total number of rows matching the search
	 * criteria minus the indicated start row is less than the number of rows
	 * requested. start row
	 * 
	 * @return An integer indicating the maximum number of rows to return.
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * Sets the maximum number of rows that should be returned by this query.
	 * Use in conjunction with {@link #setStart(Integer)} to return in a "paged"
	 * manner portions of the total result set that matches the search criteria.
	 * 
	 * @param rows
	 *            An integer indicating the maximum number of rows to return.
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * Returns the name of the field by which the returned results should be
	 * sorted.
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * Specifies the name of the field by which the returned results should be
	 * sorted.
	 * 
	 * @param sortField
	 *            The name of the field.
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	/**
	 * Indicates the direction (ascending or descending) by which the field
	 * specified by {@link #getSortField()} should be sorted in the returned
	 * results.
	 * 
	 * @return Returns a {@link SortDirection} enumeration indicating the sort
	 *         direction.
	 */
	public SortDirection getSortDirection() {
		return sortDirection;
	}

	/**
	 * Specifies the direction (ascending or descending) by which the field
	 * specified by {@link #getSortField()} should be sorted in the returned
	 * results.
	 * 
	 * @param sortDirection
	 *            The a {@link SortDirection} enumeration value indicating the
	 *            desired sort direction.
	 */
	public void setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}
}
