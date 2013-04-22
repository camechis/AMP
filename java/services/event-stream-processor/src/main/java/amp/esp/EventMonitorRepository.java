package amp.esp;


public interface EventMonitorRepository {

    void registerWith(EventStreamProcessor eventStreamProcessor);

}
