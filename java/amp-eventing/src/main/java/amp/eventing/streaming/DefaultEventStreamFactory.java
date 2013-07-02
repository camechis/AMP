package amp.eventing.streaming;

import cmf.eventing.IEventBus;
import cmf.eventing.patterns.streaming.IEventStream;

public class DefaultEventStreamFactory implements IEventStreamFactory {

    private DefaultStreamingBus eventBus;
    private String topic;

    @Override
    public void setEventBus(DefaultStreamingBus bus) {
        this.eventBus = bus;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public IEventStream generateEventStream() {
        return new DefaultEventStream(eventBus, topic);
    }
}
