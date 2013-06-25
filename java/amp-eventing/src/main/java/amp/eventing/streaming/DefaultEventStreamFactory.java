package amp.eventing.streaming;

import cmf.eventing.IEventBus;
import cmf.eventing.patterns.streaming.IEventStream;

public class DefaultEventStreamFactory implements IEventStreamFactory {

    private DefaultStreamingBus eventBus;

    @Override
    public void setEventBus(DefaultStreamingBus bus) {
        this.eventBus = bus;
    }

    @Override
    public IEventStream generateEventStream() {
        return new DefaultEventStream(eventBus);
    }
}
