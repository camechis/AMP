package amp.eventing.streaming;

import amp.eventing.IInboundProcessorCallback;
import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingProgressUpdater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static amp.eventing.streaming.StreamingEnvelopeConstants.*;
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
    protected ConcurrentHashMap<String, Collection<IStreamingEventItem<TEVENT>>> collectedEvents;



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
        this.collectedEvents = new ConcurrentHashMap<String, Collection<IStreamingEventItem<TEVENT>>>();
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
                    collectedEvents.put(sequenceId, new ArrayList<IStreamingEventItem<TEVENT>>());
                }
                IStreamingEventItem<TEVENT> eventItem = new StreamingEventItem<TEVENT>(event, env.getHeaders());
                collectedEvents.get(sequenceId).add(eventItem);
                IStreamingProgressUpdater notifier = eventHandler.getProgress();
                if (null != notifier) {
                    notifier.updateProgress(sequenceId,
                            collectedEvents.get(sequenceId).size());
                }

                if (isLast) {
                    result = this.eventHandler.handleCollection(new ArrayList<IStreamingEventItem<TEVENT>>(
                            collectedEvents.get(sequenceId)), env.getHeaders());
                    collectedEvents.remove(sequenceId);
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
