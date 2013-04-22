package pegasus.eventbus.client;

public class EventResult {

	private String name;

	private EventResult(String name) {
		this.name = name;
	}

	public static final EventResult Handled = new EventResult("Handled");

}
