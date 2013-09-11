package amp.eventing.streaming;

import amp.messaging.EnvelopeHelper;
import amp.messaging.MessageContext;
import amp.messaging.IContinuationCallback;
import amp.messaging.MessageException;
import cmf.bus.Envelope;
import cmf.eventing.patterns.streaming.IEventStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DefaultEventStream implements IEventStream {
    protected static final Logger log = LoggerFactory.getLogger(DefaultEventStream.class);
    private final IStandardStreamingEventBus eventBus;
    private int batchLimit = 2;
    private Queue<EventStreamQueueItem> queuedEvents;
    private final UUID sequenceId;
    private int position;
    private String topic;

    public DefaultEventStream(IStandardStreamingEventBus eventBus, String topic) {
        this.eventBus = eventBus;
        this.topic = topic;
        this.queuedEvents = new LinkedList<EventStreamQueueItem>();
        this.sequenceId = UUID.randomUUID();
        this.position = 0;
    }

    @Override
    public void setBatchLimit(int numberOfEvents) {
        this.batchLimit = numberOfEvents;
    }

    @Override
    public void publish(Object event) throws Exception {
        log.debug("enter publish to stream");
        String sequence = sequenceId.toString();


        Envelope env = StreamingEnvelopeHelper.buildStreamingEnvelope(sequence, position);
        EnvelopeHelper envHelper = new EnvelopeHelper(env);
        envHelper.setMessageTopic(getTopic());

        MessageContext context = new MessageContext(MessageContext.Directions.Out, event, env);
        EventStreamQueueItem eventItem = new EventStreamQueueItem(context);

        log.debug("buffering event with sequenceId: " + sequence + ", position: " + position);
        this.queuedEvents.add(eventItem);

        if (this.queuedEvents.size() == this.batchLimit) {
            log.debug("flushing " + batchLimit + " event(s) to stream.");
            flushStreamBuffer();
        }

        position++;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public String getSequenceId() {
        return this.sequenceId.toString();
    }

    /**
     * When processing a stream of an unknown size, it becomes a challenge to know when you have dealt with the
     * last object in that stream. This class utilizes the dispose() method to indicate that stream processing
     * has completed. This is necessary in order to mark the last message with the isLast flag
     * set to true. The trick here is to ensure that the streamBuffer is not entirely empty when
     * dispose gets called.
     */
    private void flushStreamBuffer() throws Exception {

        while (queuedEvents.size() > 0) {
            final EventStreamQueueItem eventItem = queuedEvents.remove();
            eventBus.processMessage(eventItem.getMessageContext(),
                    new IContinuationCallback() {
                        @Override
                        public void continueProcessing() throws MessageException {
                            try {
								eventBus.getEnvelopeBus().send(eventItem.getEnvelope());
							} catch (Exception ex) {
								log.error("Error sending streaming eventItem.", ex);
								throw new MessageException("Error sending streaming eventItem.", ex);
							}
                        }
                    });
        }
    }

    /**
     * Flushes remaining items in the buffer. This MUST be called to close out the stream or
     * the subscriber will never be notified with the last item in the sequence.
     */
    @Override
    public void dispose() {
        try {
            flushStreamBuffer();
            eventBus.publish(new EndOfStream(this.getTopic(), sequenceId.toString()));

        } catch (Exception e) {
            log.error("Unable to send last batch of messages in buffer to event stream.", e);
        }
    }

}
