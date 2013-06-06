package amp.eventing;

import cmf.eventing.IEventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: jholmberg
 * Date: 6/5/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamingIteratorRegistration extends EventRegistration {
    public StreamingIteratorRegistration(IEventHandler eventHandler, IInboundProcessorCallback processorCallback) {
        super(eventHandler, processorCallback);
    }
}
