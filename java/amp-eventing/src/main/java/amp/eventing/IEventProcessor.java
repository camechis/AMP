package amp.eventing;


public interface IEventProcessor {

	void processEvent(EventContext context, IContinuationCallback continuation) throws Exception;
	
}
