package amp.eventing.streaming;


import amp.eventing.*;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;
import cmf.eventing.patterns.streaming.*;

import java.util.*;

public class DefaultStreamingBus extends DefaultEventBus implements IStreamingEventBus, IInboundProcessorCallback {
    private IEventStreamFactory eventStreamFactory;
    private Map<String, IEventStream> eventStreams;
    /**
     * Default setting for {@link cmf.eventing.patterns.streaming.IStreamingEventBus} to stream events in batches.
     * Allows for tuning by setting this to a different value based on size of events.
     */
    protected int batchLimit = 10;

    public DefaultStreamingBus(IEnvelopeBus envelopeBus) {
        super(envelopeBus);
        initializeDefaults();
    }

    public DefaultStreamingBus(IEnvelopeBus envelopeBus, List<IEventProcessor> inboundProcessors,
                               List<IEventProcessor> outboundProcessors) {
        super(envelopeBus, inboundProcessors, outboundProcessors);
        initializeDefaults();
    }


    public DefaultStreamingBus(IEnvelopeBus envelopeBus, List<IEventProcessor> inboundProcessors,
                               List<IEventProcessor> outboundProcessors,
                               IEventStreamFactory eventStreamFactory) {
        super(envelopeBus, inboundProcessors, outboundProcessors);
        this.eventStreamFactory = eventStreamFactory;
        this.eventStreams = new HashMap<String, IEventStream>();
    }

    private void initializeDefaults() {
        this.eventStreamFactory = new DefaultEventStreamFactory();
        this.eventStreamFactory.setEventBus(this);
        this.eventStreams = new HashMap<String, IEventStream>();
    }

    @Override
    public IEventStream createStream(String topic) {
        if (false == this.eventStreams.containsKey(topic)) {
            eventStreamFactory.setTopic(topic);
            IEventStream eventStream = eventStreamFactory.generateEventStream();
            this.eventStreams.put(topic, eventStream);
            return eventStream;
        } else {
            return this.eventStreams.get(topic);
        }

    }

    public void removeStream(String topic) {
        if (this.eventStreams.containsKey(topic)) {
            this.eventStreams.remove(topic);
        }
    }

    @Override
    public <TEVENT> void publishChunkedSequence(Iterator<Object> dataSet,
                                                IStreamingMapperCallback<TEVENT> objectMapper) throws Exception {
        log.debug("enter publish to chunked sequence");
        IEventStream eventStream = null;
        String topic = null;

        validateEventIterator(dataSet);
        boolean doMap = isValidMapper(objectMapper);

        try {
            while (dataSet.hasNext()) {
                Object eventItem = dataSet.next();
                if (doMap) {
                    //This may look a little strange, but just reusing eventItem again for efficiency.
                    //The eventItem is now of type TEVENT which was converted to conform to the sender's desired format
                    //before getting serialized
                    eventItem = objectMapper.map(eventItem);
                }

                if (null == eventStream) {
                    topic = eventItem.getClass().getCanonicalName();
                    eventStream = new DefaultEventStream(this, topic); //Skipping use of the factory so that we ensure sequencing based event stream is used
                    eventStream.setBatchLimit(this.batchLimit);
                    eventStreams.put(topic, eventStream);
                }

                eventStream.publish(eventItem);
            }
        } finally {
            if (null != eventStream) {
                eventStream.dispose();
                removeStream(topic);
            }
        }
        log.debug("leave publish to chunked sequence");
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

    private void validateEventIterator(Iterator<Object> eventStream) throws Exception {
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

    @Override
    public <TEVENT> void subscribeToCollection(IStreamingCollectionHandler<TEVENT> handler) throws Exception {
        Class<TEVENT> type = handler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(handler, filterPredicate);
    }

    @Override
    public <TEVENT> void subscribeToReader(IStreamingReaderHandler<TEVENT> handler) throws Exception {
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

        } else if (eventHandler instanceof IStreamingReaderHandler) {
            StreamingReaderRegistration registration = new StreamingReaderRegistration(
                    (IStreamingReaderHandler)eventHandler, this);
            envelopeBus.register(registration);
        }
    }

    public List<IEventProcessor> getInboundProcessors() {
        return this.inboundProcessors;
    }

    public List<IEventProcessor> getOutboundProcessors() {
        return this.outboundProcessors;
    }

    public IEnvelopeBus getEnvelopeBus() {
        return this.envelopeBus;
    }


}
