package amp.eventing.streaming;

import amp.messaging.MessageContext;
import cmf.bus.Envelope;

/**
 * Contains the elements needed to send an event in an event stream
 * User: jholmberg
 * Date: 6/3/13
 */
public class EventStreamQueueItem {

    private final MessageContext context;

    public EventStreamQueueItem(MessageContext context) {
        this.context = context;
    }

    public Envelope getEnvelope() {
        return this.context.getEnvelope();
    }

    public MessageContext getMessageContext() {
        return this.context;
    }
}
