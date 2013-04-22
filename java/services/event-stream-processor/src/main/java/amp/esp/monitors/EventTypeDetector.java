package amp.esp.monitors;

import java.util.Collection;
import java.util.HashSet;

import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import amp.esp.publish.Publisher;

import pegasus.eventbus.client.Envelope;

import com.espertech.esper.client.EventBean;

public class EventTypeDetector extends EventMonitor {

    private String eventType;

    public EventTypeDetector(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public InferredEvent receive(EventBean eventBean) {
        Envelope env = (Envelope) eventBean.get("resp");
        return makeInferredEvent().addEnvelope(env);
    }

    @Override
    public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {
        esp.monitor(true, "select resp from Envelope as resp where eventType = '" + eventType + "'", this);

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
