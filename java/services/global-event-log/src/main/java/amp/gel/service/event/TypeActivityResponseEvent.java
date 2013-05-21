package amp.gel.service.event;

import amp.gel.domain.Table;

public class TypeActivityResponseEvent {
	private Table eventsByTime;

	private Table eventsByUser;

	public TypeActivityResponseEvent(Table eventsByTime, Table eventsByUser) {
		super();

		this.eventsByTime = eventsByTime;
		this.eventsByUser = eventsByUser;
	}

	public Table getEventsByTime() {
		return eventsByTime;
	}

	public Table getEventsByUser() {
		return eventsByUser;
	}
}
