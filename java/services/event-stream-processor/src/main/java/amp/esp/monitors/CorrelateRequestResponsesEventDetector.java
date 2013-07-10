package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import cmf.bus.Envelope;

import com.espertech.esper.client.EventBean;

public class CorrelateRequestResponsesEventDetector extends EventMonitor {

    public static final String INFERRED_TYPE = "RequestResponse";

    @Override
    public InferredEvent receive(EventBean eventBean) {
        Envelope req = getEnvelopeFromBean(eventBean, "request");
        Envelope resp = getEnvelopeFromBean(eventBean, "response");
        return makeInferredEvent().addEnvelope(req).addEnvelope(resp);
    }

    @Override
    public void registerPatterns(EventStreamProcessor esp) {

//        matching("EventType", eventType)

//        String pattern2 = "every request=Envelope(eventType='Request')" +
//                " -> response=Envelope(eventType='Response' and " +
//                "correlationId=request.id)";
//        String pattern = "every request=Envelope(request." +
//                matching("EventType", "Request") +
//        		")" +
//                " -> response=Envelope(response." +
//                matching("EventType", "Response") +
//                " and " + "correlationId=request.id)";
//        esp.monitor(false, pattern, this);

        EventMatcher em = EventMatcher.everyEnvelope("request").matching("EventType", "Request")
                .followedBy(EventMatcher.everyEnvelope("response")
                        .matching("EventType", "Response")
                        .matchingRef("CorrelationId", "request", "Id"));
        esp.monitor(em, this);
    }

    @Override
    public String getInferredType() {
        return INFERRED_TYPE;
    }

}
