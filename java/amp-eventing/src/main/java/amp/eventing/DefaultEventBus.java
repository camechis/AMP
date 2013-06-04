package amp.eventing;


import java.util.*;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.eventing.EventContext.Directions;


public class DefaultEventBus implements IEventBus, IStreamingEventBus, IInboundProcessorCallback {

    protected static final Logger log = LoggerFactory.getLogger(DefaultEventBus.class);
    protected IEnvelopeBus envelopeBus;
    protected List<IEventProcessor> inboundProcessors = new LinkedList<IEventProcessor>();
    protected List<IEventProcessor> outboundProcessors = new LinkedList<IEventProcessor>();
    protected int batchLimit = 10;

    public DefaultEventBus(IEnvelopeBus envelopeBus) {
        this.envelopeBus = envelopeBus;
        inboundProcessors = new ArrayList<IEventProcessor>();
        outboundProcessors = new ArrayList<IEventProcessor>();
    }

    public DefaultEventBus(IEnvelopeBus envelopeBus, List<IEventProcessor> inboundProcessors,
                           List<IEventProcessor> outboundProcessors) {
        this.envelopeBus = envelopeBus;
        this.inboundProcessors = inboundProcessors;
        this.outboundProcessors = outboundProcessors;
    }


    @Override
    public void dispose() {
        envelopeBus.dispose();
    }

    public void processEvent(
            final EventContext context,
            final List<IEventProcessor> processingChain,
            final IContinuationCallback onComplete) throws Exception {
        log.debug("Enter processEvent");

        // if the chain is null or empty, complete processing
        if ((null == processingChain) || (0 == processingChain.size())) {
            log.debug("event processing complete");
            onComplete.continueProcessing();
            return;
        }

        // get the first processor
        IEventProcessor processor = processingChain.get(0);

        // create a processing chain that no longer contains this processor
        final List<IEventProcessor> newChain = processingChain.subList(1, processingChain.size());

        // let it process the event and pass its "next" processor: a method that
        // recursively calls this function with the current processor removed
        processor.processEvent(context, new IContinuationCallback() {

            @Override
            public void continueProcessing() throws Exception {
                processEvent(context, newChain, onComplete);
            }

        });

        log.debug("Leave processEvent");
    }

    @Override
    public void publish(Object event) throws Exception {

        log.debug("enter publish");

        if (event == null) {
            throw new IllegalArgumentException("Cannot publish a null event");
        }

        final EventContext context = new EventContext(Directions.Out, new Envelope(), event);

        this.processEvent(
                context,
                outboundProcessors,
                new IContinuationCallback() {
                    @Override
                    public void continueProcessing() throws Exception {
                        envelopeBus.send(context.getEnvelope());
                    }
                });

        log.debug("leave publish");
    }

    public static final String SEQUENCE_ID = "sequenceId";
    public static final String POSITION = "position";
    public static final String IS_LAST = "isLast";

    @Override
    public <TEVENT> void publishToStream(Iterator<Object> eventStream,
                                         IStreamingMapperCallback<TEVENT> objectMapper) throws Exception {
        log.debug("enter publish to stream");

        if (null == eventStream) {
            throw new IllegalArgumentException("Cannot publish a null event stream");
        }

        boolean doMap = true;
        if (null == objectMapper) {
            log.warn("No IStreamingMapperCallback supplied. Will not perform mapping of elements in event stream");
            doMap = false;
        }


        String sequenceId = UUID.randomUUID().toString();
        int position = 0;
        boolean isLast;
        int limitCounter = 0;
        Queue<EventStreamQueueItem> streamQueue = new LinkedList<EventStreamQueueItem>();

        while (eventStream.hasNext()) {
            Object eventItem = eventStream.next();
            isLast = (!eventStream.hasNext()) ?  true : false;
            if (doMap) {
                //This may look a little strange, but just reusing eventItem again for efficiency
                //eventItem is now of type TEVENT which was converted to conform to the sender's desired format
                //before getting serialized
                eventItem = objectMapper.map(eventItem);
            }


            if ((limitCounter < this.batchLimit) || false == isLast) {
                Envelope envelope = buildStreamingEnvelope(sequenceId, position, Boolean.toString(isLast));
                EventContext context = new EventContext(Directions.Out, envelope, eventItem);
                streamQueue.add(new EventStreamQueueItem(context));
                limitCounter++;
                log.debug("Buffering event to be streamed with sequenceId: " + sequenceId + ", position: " + position + ", isLast: " + isLast);
            } else {
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
                    log.debug("Sending streamed event with sequenceId: " +
                            queueItem.getEnvelope().getHeader(SEQUENCE_ID) +
                            ", position: " + queueItem.getEnvelope().getHeader(POSITION) +
                            ", isLast: " + queueItem.getEnvelope().getHeader(IS_LAST));

                }
                limitCounter = 0;
                streamQueue.clear();
            }
            position++;


        }
        log.debug("leave publish to stream");
    }

    /**
     * Publishes messages on the {@link cmf.eventing.IStreamingEventBus} after the numberOfEvents limit has been met.
     *
     * @param numberOfEvents
     */
    @Override
    public void setBatchLimit(int numberOfEvents) {
        if (numberOfEvents <= 0) {
            log.warn("Message batch limit cannot be less than zero, using 10 as the default limit.");
            this.batchLimit = 10;
        } else {
            this.batchLimit = numberOfEvents;
        }
    }

    protected Envelope buildStreamingEnvelope(String sequenceId, int position, String isLast) {
        Envelope envelope = new Envelope();
        envelope.setHeader(SEQUENCE_ID, sequenceId);
        envelope.setHeader(POSITION, position + "");
        envelope.setHeader(IS_LAST, isLast);
        return envelope;
    }

    @Override
    public <TEVENT> void subscribe(IEventHandler<TEVENT> eventHandler) throws Exception {
        Class<TEVENT> type = eventHandler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(eventHandler, filterPredicate);
    }

    @Override
    public <TEVENT> void subscribe(final IEventHandler<TEVENT> eventHandler, final IEventFilterPredicate filterPredicate)
            throws Exception {
        EventRegistration registration = new EventRegistration(eventHandler, this);
        envelopeBus.register(registration);
    }

    @Override
    public Object ProcessInbound(Envelope envelope) throws Exception {
        final EventContext context = new EventContext(Directions.In, envelope);

        this.processEvent(
                context,
                this.inboundProcessors,
                new IContinuationCallback() {

                    @Override
                    public void continueProcessing() {
                        log.info("Completed inbound processing - returning event");
                    }
                });

        return context.getEvent();
    }


    @Override
    protected void finalize() {
        dispose();
    }


}
