package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import amp.esp.publish.Publisher;

import java.util.Collection;
import java.util.HashSet;

import pegasus.eventbus.client.WrappedEnvelope;

import com.espertech.esper.client.EventBean;

public class EventTypeDetector extends EventMonitor {

    private String eventType;

    public EventTypeDetector(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public InferredEvent receive(EventBean eventBean) {
        WrappedEnvelope env = getEnvelopeFromBean(eventBean, "resp");
        return makeInferredEvent().addEnvelope(env);
    }

    @Override
    public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {
        esp.monitor(EventMatcher.selectEnvelope("resp").matching("EventType", eventType), this);

        // @todo = this needs to be integrated
        return new HashSet<Publisher>();
    }

    @Override
    public String getInferredType() {
        return eventType;
    }

    @Override
    public String getLabel() {
        return this.getClass().getSimpleName() + "(" + eventType + ")";
    }

}
