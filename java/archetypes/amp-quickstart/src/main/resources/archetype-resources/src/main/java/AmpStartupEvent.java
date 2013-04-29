package ${package};

public class AmpStartupEvent {
	
	private long startTime = System.currentTimeMillis();

	public long getStartTime() {
		
		return startTime;
	}
	
	@Override
	public String toString() {
		
		return "AmpStartupEvent [startTime=" + startTime + "]";
	}
}