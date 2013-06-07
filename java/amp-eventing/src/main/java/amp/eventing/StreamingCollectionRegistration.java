package amp.eventing;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;
import cmf.eventing.patterns.streaming.IStreamingProgressNotifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Specialized {@link cmf.bus.IRegistration} that handles the event by aggregating events from a common sequence
 * and publishing them to a {@link java.util.Collection}.
 * User: jholmberg
 * Date: 6/5/13
 */
public class StreamingCollectionRegistration<TEVENT> implements IRegistration {
    protected IStreamingCollectionHandler<TEVENT> eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected Map<String, String> registrationInfo;
    protected ConcurrentHashMap<String, Collection<TEVENT>> collectedEvents;

    protected static final String SEQUENCE_ID = "sequenceId";
    protected static final String POSITION = "position";
    protected static final String IS_LAST = "isLast";

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    public StreamingCollectionRegistration(IStreamingCollectionHandler<TEVENT> handler, IInboundProcessorCallback processorCallback) {
        this.eventHandler = handler;
        this.processorCallback = processorCallback;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
        this.collectedEvents = new ConcurrentHashMap<String, Collection<TEVENT>>();
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        TEVENT event = (TEVENT) this.processorCallback.ProcessInbound(env);
        Object result = null;

        if (null != event) {
            try {
                String sequenceId = env.getHeader(SEQUENCE_ID);
                boolean isLast = Boolean.parseBoolean(env.getHeader(IS_LAST));

                if (!collectedEvents.containsKey(sequenceId)) {
                    collectedEvents.put(sequenceId, new ArrayList<TEVENT>());
                }

                collectedEvents.get(sequenceId).add(event);
                IStreamingProgressNotifier notifier = eventHandler.getProgressNotifier();
                if (null != notifier) {
                    notifier.updateProgress(sequenceId,
                            collectedEvents.get(sequenceId).size());
                }

                if (isLast) {
                    result = this.eventHandler.handle(collectedEvents.get(sequenceId), env.getHeaders());
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
