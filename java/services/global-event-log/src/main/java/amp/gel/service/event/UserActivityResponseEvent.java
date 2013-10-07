package amp.gel.service.event;

import amp.gel.domain.Table;

public class UserActivityResponseEvent {
	private Table eventsByTime;

	private Table eventsByType;

	public UserActivityResponseEvent(Table eventsByTime, Table eventsByType) {
		super();

		this.eventsByTime = eventsByTime;
		this.eventsByType = eventsByType;
	}

	public Table getEventsByTime() {
		return eventsByTime;
	}

	public Table getEventsByType() {
		return eventsByType;
	}
}
