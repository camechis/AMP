package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import cmf.bus.Envelope;

import com.espertech.esper.client.EventBean;

public class EventTypeDetector extends EventMonitor {

    private String eventType;

    public EventTypeDetector(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public InferredEvent receive(EventBean eventBean) {
        Envelope env = getEnvelopeFromBean(eventBean, "resp");
        return makeInferredEvent().addEnvelope(env);
    }

    @Override
    public void registerPatterns(EventStreamProcessor esp) {
        esp.monitor(EventMatcher.everyEnvelope("resp").matching("EventType", eventType), this);
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
