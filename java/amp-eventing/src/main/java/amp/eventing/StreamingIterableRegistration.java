package amp.eventing;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.patterns.streaming.IStreamingIterableHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Specialized {@link cmf.bus.IRegistration} that handles the event by placing them in a
 * {@link java.util.Iterator} from which they can be extracted as they become available.
 * User: jholmberg
 * Date: 6/5/13
 */
public class StreamingIterableRegistration<TEVENT> implements IRegistration {
    protected IStreamingIterableHandler<TEVENT> eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected  Map<String, String> registrationInfo;
    protected ConcurrentHashMap<String, Iterable<TEVENT>> eventIterators;

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

    public StreamingIterableRegistration(IStreamingIterableHandler<TEVENT> handler, IInboundProcessorCallback processorCallback) {
        this.eventHandler = handler;
        this.processorCallback = processorCallback;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
        eventIterators = new ConcurrentHashMap<String, Iterable<TEVENT>>();
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        TEVENT event = (TEVENT) this.processorCallback.ProcessInbound(env);
        Object result = null;

        if (null != event) {
            String sequenceId = env.getHeader(SEQUENCE_ID);
            boolean isLast
        }
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
