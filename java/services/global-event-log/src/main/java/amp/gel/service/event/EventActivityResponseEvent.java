package amp.gel.service.event;

import amp.gel.domain.Table;

public class EventActivityResponseEvent {
	private Table eventsByTime;

	private Table eventsByType;

	private Table eventsByUser;

	public EventActivityResponseEvent(Table eventsByTime, Table eventsByType,
			Table eventsByUser) {
		super();

		this.eventsByTime = eventsByTime;
		this.eventsByType = eventsByType;
		this.eventsByUser = eventsByUser;
	}

	public Table getEventsByTime() {
		return eventsByTime;
	}

	public Table getEventsByType() {
		return eventsByType;
	}

	public Table getEventsByUser() {
		return eventsByUser;
	}
}
