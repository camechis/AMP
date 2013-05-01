package pegasus.eventbus.client;

public class EventResult {

	private String name;

	public String getName() {
        return name;
    }

    private EventResult(String name) {
		this.name = name;
	}

	public static final EventResult Handled = new EventResult("Handled");

}
