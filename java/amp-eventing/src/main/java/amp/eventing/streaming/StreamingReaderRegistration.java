package amp.eventing.streaming;

import amp.eventing.IInboundProcessorCallback;
import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingReaderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cmf.eventing.patterns.streaming.StreamingEnvelopeConstants.*;
/**
 * Specialized {@link cmf.bus.IRegistration} that handles the event by placing them in a
 * {@link java.util.Iterator} from which they can be extracted as they become available.
 * User: jholmberg
 * Date: 6/5/13
 */
public class StreamingReaderRegistration<TEVENT> implements IRegistration {
    protected static final Logger log = LoggerFactory.getLogger(StreamingReaderRegistration.class);

    protected IStreamingReaderHandler<TEVENT> eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected  Map<String, String> registrationInfo;

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    public StreamingReaderRegistration(IStreamingReaderHandler<TEVENT> handler, IInboundProcessorCallback processorCallback) {
        this.eventHandler = handler;
        this.processorCallback = processorCallback;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        TEVENT event = (TEVENT) this.processorCallback.ProcessInbound(env);
        Object result = null;

        if (null != event) {
            try {
                boolean isLast = Boolean.parseBoolean(env.getHeader(IS_LAST));

                IStreamingEventItem<TEVENT> eventItem = new StreamingEventItem<TEVENT>(event, env.getHeaders());
                result = this.eventHandler.onEventRead(eventItem);
                if (isLast) {
                    this.eventHandler.dispose();
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
