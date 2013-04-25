package amp.esp.monitors;

import java.util.Collection;
import java.util.HashSet;

import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import amp.esp.publish.Publisher;

import pegasus.eventbus.client.WrappedEnvelope;

import com.espertech.esper.client.EventBean;

public class CorrelateRequestResponsesEventDetector extends EventMonitor {

    public static final String INFERRED_TYPE = "RequestResponse";

    @Override
    public InferredEvent receive(EventBean eventBean) {
        WrappedEnvelope req = getEnvelopeFromBean(eventBean, "request");
        WrappedEnvelope resp = getEnvelopeFromBean(eventBean, "response");
        return makeInferredEvent().addEnvelope(req).addEnvelope(resp);
    }

    @Override
    public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {

        String pattern = "every request=Envelope(eventType='Request')" +
                " -> response=Envelope(eventType='Response' and " +
                "correlationId=request.id)";
        esp.monitor(false, pattern, this);

        // @todo = this needs to be integrated
        return new HashSet<Publisher>();
    }

    @Override
    public String getInferredType() {
        return INFERRED_TYPE;
    }

}
