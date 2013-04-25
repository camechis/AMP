package amp.esp.monitors;

import java.util.Collection;
import java.util.HashSet;

import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import amp.esp.publish.Publisher;

import pegasus.eventbus.client.WrappedEnvelope;

import com.espertech.esper.client.EventBean;

public class DocumentCollectionWithHitFrequencySearchResultsDetector extends EventMonitor {

    public static final String INFERRED_TYPE = "DocumentCollectionWithHitFrequencySearchResult";

    @Override
    public InferredEvent receive(EventBean eventBean) {
        WrappedEnvelope docs = getEnvelopeFromBean(eventBean, "docs");
        WrappedEnvelope freq = getEnvelopeFromBean(eventBean, "freq");
        InferredEvent resultingEvent = makeInferredEvent();
        resultingEvent.addEnvelope(docs).addEnvelope(freq);
        return resultingEvent;
    }

    @Override
    public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {

        String pattern = "every docs=Envelope(eventType='DocumentCollectionSearchResult')" +
                " -> freq=Envelope(eventType='HitFrequencySearchResult' and " +
                "correlationId=docs.correlationId)";
        esp.monitor(false, pattern, this);

        String pattern2 = "every freq=Envelope(eventType='HitFrequencySearchResult')" +
                " -> docs=Envelope(eventType='DocumentCollectionSearchResult' and " +
                "correlationId=freq.correlationId)";
        esp.monitor(false, pattern2, this);

        // @todo = this needs to be integrated
        return new HashSet<Publisher>();
    }

    @Override
    public String getInferredType() {
        return INFERRED_TYPE;
    }

}
