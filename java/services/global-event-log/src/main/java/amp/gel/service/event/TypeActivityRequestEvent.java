package amp.gel.service.event;

import java.util.Date;

import amp.gel.domain.TimeScale;

public class TypeActivityRequestEvent {

	private String type;

	private Date start;

	private Date stop;

	private TimeScale timeScale;

	public TypeActivityRequestEvent(String type, Date start, Date stop,
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
