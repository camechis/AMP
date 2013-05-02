package amp.esp;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;

import java.util.Map;

public class ESPRegistration implements IRegistration {

    private EventStreamProcessor eventStreamProcessor;

    public ESPRegistration(EventStreamProcessor eventStreamProcessor) {
        this.eventStreamProcessor = eventStreamProcessor;
    }

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object handle(Envelope env) throws Exception {
        eventStreamProcessor.sendEvent(env);
        return null;
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}