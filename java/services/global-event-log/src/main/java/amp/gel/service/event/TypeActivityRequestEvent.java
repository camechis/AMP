package amp.gel.service.event;

import org.joda.time.DateTime;

import amp.gel.domain.TimeScale;

public class TypeActivityRequestEvent {

	private String type;

	private DateTime start;

	private DateTime stop;

	private TimeScale timeScale;

	public TypeActivityRequestEvent(String type, DateTime start, DateTime stop,
			TimeScale timeScale) {
		super();

		this.type = type;
		this.start = start;
		this.stop = stop;
		this.timeScale = timeScale;
	}

	public String getType() {
		return type;
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
