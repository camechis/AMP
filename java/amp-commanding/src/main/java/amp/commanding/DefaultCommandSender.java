package amp.commanding;


import java.util.List;

import cmf.bus.IEnvelopeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandSender implements ICommandSender, ICommandChainProcessor {

    static final Logger LOG = LoggerFactory.getLogger(DefaultCommandSender.class);

    private IEnvelopeSender _envelopeSender;
    private List<ICommandProcessor> _processorChain;


    public DefaultCommandSender(IEnvelopeSender envelopeSender) {
        _envelopeSender = envelopeSender;
    }

    public DefaultCommandSender(IEnvelopeSender envelopeSender, List<ICommandProcessor> processorChain) {
        _envelopeSender = envelopeSender;
        _processorChain = processorChain;
    }


    @Override
    public void send(Object command) throws CommandException {

        if (null == command) { throw new IllegalArgumentException("Cannot send a null command."); }
        LOG.debug("Enter send");

        final CommandContext context = new CommandContext(CommandContext.Directions.Out, command);

        try {

            this.processCommand(context, _processorChain, new IContinuationCallback() {

                @Override
                public void continueProcessing() throws Exception {
                    _envelopeSender.send(context.getEnvelope());
                }
            });
        }
        catch(Exception ex) {
            String message = "Caught an exception while processing command.";
            LOG.warn(message, ex);
            throw new CommandException(message, ex);
        }

        LOG.debug("Leave send");
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
    }
}
