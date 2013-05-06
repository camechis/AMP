package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;

import java.util.List;

import com.espertech.esper.client.EventBean;

/**
 * Support class for testing monitors that generate Inferred Events.
 * This takes a List for storing InferredEvent objects, and catches and stores
 * all generated InferredEvents into it.
 *
 * @author israel
 *
 */
public class InferredEventCatcher extends EventMonitor {

    private final List<InferredEvent> detected;

    public InferredEventCatcher(List<InferredEvent> detected) {
        this.detected = detected;
    }

    @Override
    public InferredEvent receive(EventBean eventBean) {
        InferredEvent env = (InferredEvent) eventBean.get("res");
        detected.add(env);
        return null;
    }

    @Override
    public void registerPatterns(EventStreamProcessor esp) {
        esp.monitor(EventMatcher.selectInferredEvent("res"), this);
    }
}
