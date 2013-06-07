package amp.eventing;


import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;
import cmf.eventing.patterns.streaming.IStreamingEventBus;
import cmf.eventing.patterns.streaming.IStreamingIterableHandler;
import cmf.eventing.patterns.streaming.IStreamingMapperCallback;

import java.util.*;

public class DefaultStreamingBus extends DefaultEventBus implements IStreamingEventBus, IInboundProcessorCallback {

    public DefaultStreamingBus(IEnvelopeBus envelopeBus) {
        super(envelopeBus);
    }

    public DefaultStreamingBus(IEnvelopeBus envelopeBus, List<IEventProcessor> inboundProcessors,
                               List<IEventProcessor> outboundProcessors) {
        super(envelopeBus, inboundProcessors, outboundProcessors);
    }

    /**
     * Iterates on the eventStream and calls the object mapper to convert the object to the desired format before
     * publishing the event to the bus.
     * <p>
     *     In addition 3 new headers will be added to each event that is published:<br />
     *     <ol>
     *         <li>sequenceId : A UUID that ties this event to a particular sequence. This is the unique identifier that
     *         indicates the message is part of a larger data set</li>
     *         <li>position : An integer that indicates what position in the sequence this is.</li>
     *         <li>isLast : A boolean indicating if this event is the last message in the sequence</li>
     *     </ol>
     * </p>
     * <p>Remember, don't cross the streams!</p>
     * @param eventStream
     * @param objectMapper
     * @param <TEVENT>
     * @throws Exception
     */
    @Override
    public <TEVENT> void publishStream(Iterator<Object> eventStream,
                                       IStreamingMapperCallback<TEVENT> objectMapper) throws Exception {
        log.debug("enter publish to stream");

        validateEventStream(eventStream);
        boolean doMap = isValidMapper(objectMapper);

        String sequenceId = UUID.randomUUID().toString();
        int position = 0;
        boolean isLast;
        Queue<EventStreamQueueItem> streamBuffer = new LinkedList<EventStreamQueueItem>();

        while (eventStream.hasNext()) {
            Object eventItem = eventStream.next();
            isLast = (!eventStream.hasNext()) ? true : false;
            if (doMap) {
                //This may look a little strange, but just reusing eventItem again for efficiency.
                //The eventItem is now of type TEVENT which was converted to conform to the sender's desired format
                //before getting serialized
                eventItem = objectMapper.map(eventItem);
            }

            Envelope envelope = buildStreamingEnvelope(sequenceId, position, Boolean.toString(isLast));
            EventContext context = new EventContext(EventContext.Directions.Out, envelope, eventItem);
            streamBuffer.add(new EventStreamQueueItem(context));

            log.debug("Buffering event to be sequenced with sequenceId: " + sequenceId + ", position: " + position + ", isLast: " + isLast);

            //Flush bufffer if batch limit has been met
            if (streamBuffer.size() == batchLimit || isLast) {
                flushBufferToStream(streamBuffer);
                streamBuffer.clear();
            }
            position++;
        }
        log.debug("leave publish to sequence");
    }

    /**
     * Publishes messages on the {@link cmf.eventing.patterns.streaming.IStreamingEventBus} after the numberOfEvents limit has been met.
     *
     * @param numberOfEvents
     */
    @Override
    public void setBatchLimit(int numberOfEvents) {
        if (numberOfEvents <= 0) {
            log.warn("Message batch limit cannot be less than or equal to zero, using 10 as the default limit.");
            this.batchLimit = 10;
        } else {
            this.batchLimit = numberOfEvents;
        }
    }

    private void validateEventStream(Iterator<Object> eventStream) throws Exception {
        if (null == eventStream) {
            throw new IllegalArgumentException("Cannot publish a null event stream");
        }
    }

    private <TEVENT> boolean isValidMapper(IStreamingMapperCallback<TEVENT> objectMapper) {
        boolean doMap = true;
        if (null == objectMapper) {
            log.warn("No IStreamingMapperCallback supplied. Will not perform transformational mapping of elements in event stream");
            doMap = false;
        }
        return doMap;
    }

    protected Envelope buildStreamingEnvelope(String sequenceId, int position, String isLast) {
        Envelope envelope = new Envelope();
        envelope.setHeader(SEQUENCE_ID, sequenceId);
        envelope.setHeader(POSITION, position + "");
        envelope.setHeader(IS_LAST, isLast);
        return envelope;
    }

    private void flushBufferToStream(Queue<EventStreamQueueItem> streamQueue) throws Exception {
        while (streamQueue.size() > 0) {
            final EventStreamQueueItem queueItem = streamQueue.remove();
            this.processEvent(queueItem.getEventContext(),
                    outboundProcessors,
                    new IContinuationCallback() {
                        @Override
                        public void continueProcessing() throws Exception {
                            envelopeBus.send(queueItem.getEnvelope());
                        }
                    });
            Envelope env = queueItem.getEnvelope();
            String seqId = env.getHeader(SEQUENCE_ID);
            String pos = env.getHeader(POSITION);
            String last = env.getHeader(IS_LAST);
            log.debug("Sending sequenced event with sequenceId: " + seqId + ", position: " + pos + ", isLast: " + last);
        }
    }

    /**
     * Subscribe to a stream of events that will get handled to a {@link java.util.Collection}.
     * <p>See {@link cmf.eventing.patterns.streaming.IStreamingCollectionHandler#numEventsHandled()}</p> as a way to
     * get a progress check on how many events have been processed.
     * <p>
     * This offers the subscriber a simpler API allowing them to get the entire collection
     * of events once the last one has been received from the producer.
     * </p>
     *
     * @param handler
     * @param <TEVENT>
     * @throws Exception
     */
    @Override
    public <TEVENT> void subscribeToCollection(IStreamingCollectionHandler<TEVENT> handler) throws Exception {
        Class<TEVENT> type = handler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(handler, filterPredicate);
    }

    /**
     * Subscribe to a stream of events that will get handled to a {@link java.util.Iterator}.
     * <p>
     * This offers the subscriber lower latency API to immediately pull events from a
     * stream as they arrive.
     * </p>
     *
     * @param handler
     * @param <TEVENT>
     * @throws Exception
     */
    @Override
    public <TEVENT> void subscribeToIterator(IStreamingIterableHandler<TEVENT> handler) throws Exception {
        Class<TEVENT> type = handler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(handler, filterPredicate);
    }

    @Override
    public <TEVENT> void subscribe(final IEventHandler<TEVENT> eventHandler, final IEventFilterPredicate filterPredicate)
            throws Exception {

        if (eventHandler instanceof IStreamingCollectionHandler) {
            StreamingCollectionRegistration registration = new StreamingCollectionRegistration(
                    (IStreamingCollectionHandler)eventHandler, this);
            envelopeBus.register(registration);

        } else if (eventHandler instanceof IStreamingIterableHandler) {
            StreamingIterableRegistration registration = new StreamingIterableRegistration(
                    (IStreamingIterableHandler)eventHandler, this);
            envelopeBus.register(registration);
        }
    }
}
