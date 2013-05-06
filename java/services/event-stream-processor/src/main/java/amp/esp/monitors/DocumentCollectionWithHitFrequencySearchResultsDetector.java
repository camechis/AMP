package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import cmf.bus.Envelope;

import com.espertech.esper.client.EventBean;

public class DocumentCollectionWithHitFrequencySearchResultsDetector extends EventMonitor {

    public static final String INFERRED_TYPE = "DocumentCollectionWithHitFrequencySearchResult";

    @Override
    public InferredEvent receive(EventBean eventBean) {
        Envelope docs = getEnvelopeFromBean(eventBean, "docs");
        Envelope freq = getEnvelopeFromBean(eventBean, "freq");
        InferredEvent resultingEvent = makeInferredEvent();
        resultingEvent.addEnvelope(docs).addEnvelope(freq);
        return resultingEvent;
    }

    @Override
    public void registerPatterns(EventStreamProcessor esp) {

        String env1 = "docs";
        String env2 = "freq";
        String type1 = "DocumentCollectionSearchResult";
        String type2 = "HitFrequencySearchResult";

        EventMatcher em = EventMatcher.everyEnvelope(env1) .matching("EventType", type1)
                .followedBy(EventMatcher.everyEnvelope(env2).matching("EventType", type2)
                        .matchingRef("CorrelationId", env1, "CorrelationId"));
        esp.monitor(em, this);

        EventMatcher em2 = EventMatcher.everyEnvelope(env2) .matching("EventType", type2)
                .followedBy(EventMatcher.everyEnvelope(env1).matching("EventType", type1)
                        .matchingRef("CorrelationId", env2, "CorrelationId"));
        esp.monitor(em2, this);
    }

    @Override
    public String getInferredType() {
        return INFERRED_TYPE;
    }

}
