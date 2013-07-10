package amp.gel.dao.impl.derby.data.events.core;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * This is the base event type from which all over event types in Orion inherit.
 */
public abstract class Event implements Serializable {

	private static final long serialVersionUID = 4298474573420410702L;

	/**
	 * The unique identifier of this {@link Event}.
	 */
	private UUID id;

	private long timeCreated;

	protected Event() {
		setId(UUID.randomUUID());
		timeCreated = new Date().getTime();
	}

	/**
	 * Sets the unique ID of the content. This value need not be set for new
	 * events as it will be set automatically by the constructor. This setter is
	 * primarily intended for use in deserializing existing events.
	 * 
	 * @param uuid
	 *            the UUID of the event.
	 */
	public void setId(final UUID uuid) {
		id = uuid;
	}

	/**
	 * Get the unique ID of the content. It cannot be {@code null}, but its
	 * uniqueness must be enforced elsewhere.
	 * 
	 * @return the content's ID.
	 */
	public final UUID getId() {
		return id;
	}

	/**
	 * Sets the time created for the event. This value is set to the current
	 * system time by the constructor and need only be modified is some other
	 * time is desired (such as time sent). This setter is primarily intended
	 * for use in deserializing existing events.
	 * 
	 * @param time
	 */
	public void setTimeCreated(final long time) {
		timeCreated = time;
	}

	/**
	 * The time at which the event was initially created.
	 * 
	 * @return the timeSent
	 */
	public long getTimeCreated() {
		return timeCreated;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (timeCreated ^ (timeCreated >>> 32));
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Event)) {
			return false;
		}
		Event other = (Event) obj;
		if (timeCreated != other.timeCreated) {
			return false;
		}
		return true;
	}
}
