package amp.gel.dao.impl.derby.data.events.haruspex;

import amp.gel.dao.impl.derby.data.events.core.Event;
import amp.gel.dao.impl.derby.data.events.search.ResultEvent;

/**
 * Base class for cube request events. Perhaps it should be marked
 * {@code abstract}.
 */
public class CubeResponseEvent extends ResultEvent {

	private static final long serialVersionUID = -7559633768287929812L;

	/**
	 * The cube response string.
	 */
	private String rawCubeResponse;

	/**
	 * The {@link CubeRequestEvent} to which this response is responding.
	 */
	private CubeRequestEvent requestEvent;

	/**
	 * Get the cube response string.
	 * 
	 * @return the cube response string, which may be {@code null} if
	 *         {@link #setRawCubeResponse(String)} hasn't been called.
	 */
	public String getRawCubeResponse() {
		return rawCubeResponse;
	}

	/**
	 * Set the cube response string. This event does not validate the response
	 * data, so you must look elsewhere to find out what it has to contain.
	 * 
	 * @param rawResponse
	 *            the response string. It may be {@code null}.
	 */
	public void setRawCubeResponse(String rawResponse) {
		this.rawCubeResponse = rawResponse;
	}

	/**
	 * Get the {@link CubeRequestEvent} to which this response is responding.
	 * 
	 * @return the request event. It may be {@code null} if
	 *         {@link #setRequestEvent(CubeRequestEvent)} hasn't been called.
	 */
	public Event getRequestEvent() {
		return requestEvent;
	}

	/**
	 * Set the {@link CubeRequestEvent} to which this response is responding.
	 * 
	 * @param requestEvent
	 *            the request event. It may be {@code null}.
	 */
	public void setRequestEvent(CubeRequestEvent requestEvent) {
		this.requestEvent = requestEvent;
	}

}
