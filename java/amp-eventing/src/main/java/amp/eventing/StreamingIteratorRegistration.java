package amp.eventing;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.IEventHandler;
import cmf.eventing.patterns.streaming.IStreamingIteratorHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Specialized {@link cmf.bus.IRegistration} that handles the event by placing them in a
 * {@link java.util.Iterator} from which they can be extracted as they become available.
 * User: jholmberg
 * Date: 6/5/13
 */
public class StreamingIteratorRegistration<TEVENT> implements IRegistration {
    protected IStreamingIteratorHandler<TEVENT> eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected  Map<String, String> registrationInfo;
    protected ConcurrentHashMap<String, Iterator<TEVENT>> eventIterators;

    protected static final String SEQUENCE_ID = "sequenceId";
    protected static final String POSITION = "position";
    protected static final String IS_LAST = "isLast";

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
