package amp.gel.service.event;

import java.util.Date;

import amp.gel.domain.TimeScale;

public class UserActivityRequestEvent {

	private String user;

	private Date start;

	private Date stop;

	private TimeScale timeScale;

	public UserActivityRequestEvent(String user, Date start, Date stop,
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
