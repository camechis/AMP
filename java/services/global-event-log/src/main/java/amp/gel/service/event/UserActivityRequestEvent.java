package amp.gel.service.event;

import org.joda.time.DateTime;

import amp.gel.domain.TimeScale;

public class UserActivityRequestEvent {

	private String user;

	private DateTime start;

	private DateTime stop;

	private TimeScale timeScale;

	public UserActivityRequestEvent(String user, DateTime start, DateTime stop,
			TimeScale timeScale) {
		super();

		this.user = user;
		this.start = start;
		this.stop = stop;
		this.timeScale = timeScale;
	}

	public String getUser() {
		return user;
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
