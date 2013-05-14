package amp.commanding;


import java.util.List;
import javax.annotation.Nullable;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeReceiver;
import cmf.bus.IRegistration;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandReceiver implements ICommandReceiver, ICommandChainProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCommandReceiver.class);

    private IEnvelopeReceiver _envelopeReceiver;
    private List<ICommandProcessor> _processingChain;


    public List<ICommandProcessor> getProcessingChain() { return _processingChain; }
    public void setProcessingChain(List<ICommandProcessor> value) { _processingChain = value; }


    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver) {
        _envelopeReceiver = envelopeReceiver;
    }

    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver, List<ICommandProcessor> processingChain) {
        _envelopeReceiver = envelopeReceiver;
        _processingChain = processingChain;
    }


    @Override
    public <TCOMMAND> void onCommandReceived(ICommandHandler<TCOMMAND> handler) throws CommandException, IllegalArgumentException {

        LOG.debug("Enter onCommandReceived");
        if (null == handler) { throw new IllegalArgumentException("Cannot register a null handler"); }


        // create a registration object
        final CommandRegistration registration = new CommandRegistration(this, _processingChain, handler);

        // and register it with the envelope receiver
        try {
            _envelopeReceiver.register(registration);
        }
        catch (Exception ex) {
            String message = "Failed to register for a command";
            LOG.error(message, ex);
            throw new CommandException(message, ex);
        }


        LOG.debug("Leave onCommandReceived");
    }


    @Override
    public void processCommand(
            final CommandContext context,
            final List<ICommandProcessor> processingChain,
            final IContinuationCallback onComplete) throws Exception {

        LOG.debug("Enter processCommand");

        // if the chain is null or empty, complete processing
        if ( (null == processingChain) || (0 == processingChain.size()) ) {
            LOG.debug("command processing complete");
            onComplete.continueProcessing();
            return;
        }

        // get the first processor
        ICommandProcessor processor = processingChain.get(0);

        // create a processing chain that no longer contains this processor
        final List<ICommandProcessor> newChain = processingChain.subList(1, processingChain.size());

        // let it process the event and pass its "next" processor: a method that
        // recursively calls this function with the current processor removed
        processor.processCommand(context, new IContinuationCallback() {

            @Override
            public void continueProcessing() throws Exception {
                processCommand(context, newChain, onComplete);
            }

        });

        LOG.debug("Leave processCommand");
    }

    @Override
    public void dispose() {
        _envelopeReceiver.dispose();
    }
}
