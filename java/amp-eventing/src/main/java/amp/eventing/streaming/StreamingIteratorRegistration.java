package amp.eventing.streaming;

import amp.eventing.IInboundProcessorCallback;
import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingIteratorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static amp.eventing.streaming.StreamingEnvelopeConstants.*;
/**
 * Specialized {@link cmf.bus.IRegistration} that handles the event by placing them in a
 * {@link java.util.Iterator} from which they can be extracted as they become available.
 * User: jholmberg
 * Date: 6/5/13
 */
public class StreamingIteratorRegistration<TEVENT> implements IRegistration {
    protected static final Logger log = LoggerFactory.getLogger(StreamingIteratorRegistration.class);

    protected IStreamingIteratorHandler<TEVENT> eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected  Map<String, String> registrationInfo;
    protected ConcurrentHashMap<String, Queue<IStreamingEventItem<TEVENT>>> eventIterators;

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    public StreamingIteratorRegistration(IStreamingIteratorHandler<TEVENT> handler, IInboundProcessorCallback processorCallback) {
        this.eventHandler = handler;
        this.processorCallback = processorCallback;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
        eventIterators = new ConcurrentHashMap<String, Queue<IStreamingEventItem<TEVENT>>>();
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        TEVENT event = (TEVENT) this.processorCallback.ProcessInbound(env);
        Object result = null;

        if (null != event) {
            try {
                String sequenceId = env.getHeader(SEQUENCE_ID);
                boolean isLast = Boolean.parseBoolean(env.getHeader(IS_LAST));
                boolean isFirst = false;
                if (!eventIterators.containsKey(sequenceId)) {
                    eventIterators.put(sequenceId, new LinkedList<IStreamingEventItem<TEVENT>>());
                    isFirst  = true;
                }
                IStreamingEventItem<TEVENT> eventItem = new StreamingEventItem<TEVENT>(event, env.getHeaders());

                eventIterators.get(sequenceId).add(eventItem);

                if (isFirst) {
                    //Pass the iterator over to the handler.
                    //With a reference to the iterator, just call add for
                    //subsequent events in the sequence
                    result = this.eventHandler.handleStream(eventIterators.get(sequenceId).iterator(),
                            env.getHeaders());
                }

                if (isLast) {
                    //Once the last event in the sequence has been received, remove it
                    //to conserve memory
                    eventIterators.remove(sequenceId);
                }
            } catch (Exception ex) {
                result = handleFailed(env, ex);
            }
        }
        return result;
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        try {
            return eventHandler.handleFailed(env, ex);
        } catch (Exception failedToFail) {
            throw failedToFail;
        }
    }
}
