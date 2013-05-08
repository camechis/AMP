package amp.gel.service.event;

import org.joda.time.DateTime;

import amp.gel.domain.TimeScale;

public class EventActivityRequestEvent {

	private DateTime start;

	private DateTime stop;

	private TimeScale timeScale;

	public EventActivityRequestEvent(String type, DateTime start,
			DateTime stop, TimeScale timeScale) {
		super();

		this.start = start;
		this.stop = stop;
		this.timeScale = timeScale;
	}

	public DateTime getStart() {
		return start;
	}

	public DateTime getStop() {
		return stop;
	}

	public TimeScale getTimeScale() {
		return timeScale;
	}
}
