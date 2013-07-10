package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;

import com.espertech.esper.client.EventBean;

/**
 * Monitor for testing that catches all inferred events and logs them.
 *
 * @author israel
 *
 */
public class InferredEventPrinter extends EventMonitor {

    @Override
    public InferredEvent receive(EventBean eventBean) {
        InferredEvent event = (InferredEvent) eventBean.get("event");
        logger.info("<-- " + event);
        return null;
    }

    @Override
    public void registerPatterns(EventStreamProcessor esp) {
        esp.monitor(EventMatcher.selectInferredEvent("event"), this);
    }

    @Override
    public String getInferredType() {
        return null;
    }
}
