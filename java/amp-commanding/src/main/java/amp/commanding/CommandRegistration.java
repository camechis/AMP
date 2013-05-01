package amp.commanding;


import java.util.HashMap;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import com.google.common.base.Function;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class CommandRegistration implements IRegistration {

    private ICommandHandler<?> _handler;
    private Function<Envelope, Object> _callback;
    private Map<String, String> _regInfo;


    @Override
    public Map<String, String> getRegistrationInfo() { return _regInfo; }

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() { return null; }


    public <TCOMMAND> CommandRegistration(ICommandHandler<TCOMMAND> handler, Function<Envelope, Object> callback) {
        _handler = handler;
        _callback = callback;

        _regInfo = new HashMap<String, String>();
        _regInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, _handler.getCommandType().getCanonicalName());
        _regInfo.put(EnvelopeHeaderConstants.MESSAGE_TYPE, _handler.getCommandType().getCanonicalName());
    }


    @Override
    public Object handle(Envelope env) throws Exception {
        return _callback.apply(env);
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        return null;
    }
}
