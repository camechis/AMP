package amp.eventing;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Specialized {@link cmf.bus.IRegistration} that handles the event by aggregating events from a common sequence
 * and publishing them to a {@link java.util.Collection}.
 * User: jholmberg
 * Date: 6/5/13
 */
public class StreamingCollectionRegistration implements IRegistration {
    protected IStreamingCollectionHandler eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected Map<String, String> registrationInfo;

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    public StreamingCollectionRegistration(IStreamingCollectionHandler handler, IInboundProcessorCallback processorCallback) {
        this.eventHandler = eventHandler;
        this.processorCallback = processorCallback;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        Object event = this.processorCallback.ProcessInbound(env);
        Object result = null;

        if (null != event) {
            try {

                result = eventHandler.handle(event, env.getHeaders());
            } catch (Exception ex) {
                result = handleFailed(env, ex);
            }
        }

        return result;
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
