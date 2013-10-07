package amp.gel.dao.impl.derby.data.events.core;

/**
 * An abstract event type from which all events should inherit for which it is
 * intended that occurrences be aggregated by the Clickstream Aggregation
 * Service.
 */
public abstract class ClickstreamEvent extends Event {

	/**
	 * An enumeration that defines the set of values may be used with
	 * {@link ClickstreamEvent#getAction} and {@link ClickstreamEvent#setAction}
	 * .
	 */
	public enum Action {
		/**
		 * Indicates that the event is essentially a query operation intended to
		 * return data.
		 */
		SEARCH("query"),

		/**
		 * Indicates that the event is a response to a query operation.
		 */
		SEARCH_RESULT("query_result"),

		/**
		 * Indicates that the event is essentially a write operation intended to
		 * update data.
		 */
		UPDATE("update"),

		/**
		 * Indicates that the event is essentially a export operation that moves
		 * data to a storage medium outside of Orion.
		 */
		EXPORT("export"),

		/**
		 * Indicates that the event is a user-interface only action, such as
		 * navigation between pages.
		 */
		USER_INTERFACE("user_interface");

		private String type;

		private Action(final String typeStr) {
			type = typeStr;
		}

		/**
		 * Returns the string representation of the enumerated value, used when
		 * serializing the value to string based formats such as JSON and XML.
		 */
		public String toString() {
			return type;
		}
	}

	private static final long serialVersionUID = -8632241656100414334L;

	/**
	 * id of the destination for this event
	 */
	private String destination;

	/**
	 * web app path
	 */
	private String path;

	/**
	 * One of either {@value Action#SEARCH} or {@value Action#SEARCH_RESULT} or
	 * {@value Action#UPDATE} or {@value Action#EXPORT} or
	 * {@value Action#USER_INTERFACE} This is a field is required by the
	 * clickstream analysis services.
	 */
	private Action action;

	/**
	 * Used by implementing constructors to initialize a new instance of
	 * ClickstreamEvent.
	 */
	protected ClickstreamEvent() {
		super();
	}

	/**
	 * Used by implementing constructors to initialize a new instance of
	 * ClickstreamEvent with a particular Action value. Since events of a given
	 * type should always be of the same action, this constructor should
	 * generally be used over the default one.
	 */
	protected ClickstreamEvent(final Action eventAction) {
		action = eventAction;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Indicates the general type of action that this event represents.
	 * 
	 * @return A value of type {@link Action}.
	 */
	public Action getAction() {
		return action;
	}
}
