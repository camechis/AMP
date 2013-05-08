package amp.gel.service.event;

import java.util.Date;

import amp.gel.domain.TimeScale;

public class EventActivityRequestEvent {

	private Date start;

	private Date stop;

	private TimeScale timeScale;

	public EventActivityRequestEvent(Date start, Date stop, TimeScale timeScale) {
		super();

		this.start = start;
		this.stop = stop;
		this.timeScale = timeScale;
	}

	public Date getStart() {
		return start;
	}

	public Date getStop() {
		return stop;
	}

	public TimeScale getTimeScale() {
		return timeScale;
	}
}
