package amp.eventing.streaming;

import amp.messaging.IMessageProcessor;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.patterns.streaming.IStreamingEventBus;

public interface IStandardStreamingEventBus extends IStreamingEventBus, IMessageProcessor {
   
	IEnvelopeBus getEnvelopeBus();
}
