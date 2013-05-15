package amp.commanding;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import com.google.common.base.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class CommandRegistration implements IRegistration {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRegistration.class);

    private ICommandChainProcessor _processor;
    private List<ICommandProcessor> _processingChain;
    private ICommandHandler _handler;
    private Map<String, String> _regInfo;


    @Override
    public Map<String, String> getRegistrationInfo() { return _regInfo; }

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() { return null; }


    public CommandRegistration(
            ICommandChainProcessor processor,
            List<ICommandProcessor> processingChain,
            ICommandHandler handler) {

        _processor = processor;
        _processingChain = processingChain;
        _handler = handler;

        _regInfo = new HashMap<String, String>();
        _regInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, _handler.getCommandType().getCanonicalName());
        _regInfo.put(EnvelopeHeaderConstants.MESSAGE_TYPE, _handler.getCommandType().getCanonicalName());
    }


    @Override
    public Object handle(final Envelope env) throws Exception {

        try {
            // create a context to send through the processors
            final CommandContext ctx = new CommandContext(CommandContext.Directions.In, env);

            _processor.processCommand(ctx, _processingChain, new IContinuationCallback() {

                @Override
                public void continueProcessing() throws CommandException {
                    _handler.handle(ctx.getCommand(), env.getHeaders());
                }
            });

        }
        catch (CommandException ex) {
            String message = "Failed to process an incoming command envelope.";
            LOG.error(message, ex);
            throw new Exception(message, ex);
        }

        return null;
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        return null;
    }
}
