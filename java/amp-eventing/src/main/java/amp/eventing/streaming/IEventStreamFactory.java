package amp.eventing.streaming;

import cmf.eventing.IEventBus;
import cmf.eventing.patterns.streaming.IEventStream;

public interface IEventStreamFactory {

    void setEventBus(DefaultStreamingBus bus);

    void setTopic(String topic);

    IEventStream generateEventStream();
}
