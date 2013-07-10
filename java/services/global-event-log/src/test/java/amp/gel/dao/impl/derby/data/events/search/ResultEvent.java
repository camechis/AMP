package amp.gel.dao.impl.derby.data.events.search;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import amp.gel.dao.impl.derby.data.events.core.ClickstreamEvent;

/**
 * ResultEvent is an abstract base event type for all events that are responses
 * to a subclass of {@link SearchEvent}. ResultEvent is a subclass of
 * {@link ClickstreamEvent} that specifies {@link Action#SEARCH_RESULT} as its
 * action.
 */
public abstract class ResultEvent extends ClickstreamEvent {

	private static final long serialVersionUID = -4241313159038855860L;

	private SearchEvent search;

	// private final List<Result> results;

	private int num_records_matched;

	private int num_records_returned;

	/**
	 * The time, in milliseconds, that this query took to execute. This
	 * corresponds o the {@code time_to_process_query} field that's required by
	 * the clickstream analysis service.
	 */
	private long time_to_process_query;

	// § returned_clearance_flags: highest classification levels of any data
	// returned
	// § kb_records_returned

	/**
	 * Must be called by implementing types in order that
	 * {@link Action#SEARCH_RESULT} be specified as the Clickstream event
	 * action.
	 */
	public ResultEvent() {
		super(Action.SEARCH_RESULT);
		// results = new ArrayList<Result>();
	}

	/**
	 * Returns a copy of the original {@link SearchEvent} to which this event is
	 * a response.
	 * 
	 * This is useful when needing to correlate results with the original
	 * request or to get the search criteria for which these results were
	 * returned.
	 * 
	 * @return The originating {@link SearchEvent}.
	 */
	public SearchEvent getSearch() {
		return search;
	}

	/**
	 * Specifies the original {@link SearchEvent} to which this event is a
	 * response.
	 * 
	 * @param search
	 *            The original {@link SearchEvent}.
	 */
	public void setSearchEvent(SearchEvent search) {
		time_to_process_query = getTimeCreated() - search.getTimeCreated();
		this.search = search;
	}

	/**
	 * Provides an estimate of the elapsed time for the query based on the
	 * creation time of the original instance of the {@link SearchEvent} and the
	 * creation time of the original instance of this event.
	 * 
	 * @return The time elapsed in milliseconds.
	 */
	public long getElapsedTime() {
		return time_to_process_query;
	}

	//
	// public void addResult(final Result result) {
	// Throw.ifNull(result, "result");
	// results.add(result);
	// recalculateRecordCount();
	// }
	//
	// public final List<Result> getResults() {
	// return results;
	// }
	//
	// public void clear() {
	// results.clear();
	// recalculateRecordCount();
	// }
	//
	// private void recalculateRecordCount() {
	// num_records_matched = results.size();
	// num_records_returned = num_records_matched;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("search", search);
		return builder.toString();
	}

	/**
	 * Returns the total number of records that matched the search query.
	 */
	public final int getRecordsMatchedCount() {
		return num_records_matched;
	}

	/**
	 * Returns the number of records returned with this event.
	 */
	public final int getRecordsReturnedCount() {
		return num_records_returned;
	}

}
