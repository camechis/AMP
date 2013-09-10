package amp.commanding;


import java.util.List;

import amp.messaging.IContinuationCallback;
import amp.messaging.IMessageChainProcessor;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageContext;
import amp.messaging.MessageException;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandSender implements ICommandSender, IMessageChainProcessor {

    static final Logger LOG = LoggerFactory.getLogger(DefaultCommandSender.class);

    private IEnvelopeSender _envelopeSender;
    private List<IMessageProcessor> _processorChain;


    public DefaultCommandSender(IEnvelopeSender envelopeSender) {
        _envelopeSender = envelopeSender;
    }

    public DefaultCommandSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processorChain) {
        _envelopeSender = envelopeSender;
        _processorChain = processorChain;
    }


    @Override
    public void send(Object command) throws MessageException {

        if (null == command) { throw new IllegalArgumentException("Cannot send a null command."); }
        LOG.debug("Enter send");

        final Envelope envelope = new Envelope();
        final MessageContext context = new MessageContext(MessageContext.Directions.Out, command, envelope);

        try {

            this.processMessage(context, _processorChain, new IContinuationCallback() {

                @Override
                public void continueProcessing() throws MessageException {

                    try {
                        _envelopeSender.send(context.getEnvelope());
                    }
                    catch (Exception ex) {
                        String message = "Failed to send envelope containing command.";
                        LOG.error(message, ex);
                        throw new MessageException(message, ex);
                    }
                }
            });
        }
        catch(Exception ex) {
            String message = "Caught an exception while processing command.";
            LOG.warn(message, ex);
            throw new MessageException(message, ex);
        }

        LOG.debug("Leave send");
    }

    @Override
    public void processMessage(
            final MessageContext context,
            final List<IMessageProcessor> processingChain,
            final IContinuationCallback onComplete) throws MessageException {

        LOG.debug("Enter processCommand");

        // if the chain is null or empty, complete processing
        if ( (null == processingChain) || (0 == processingChain.size()) ) {
            LOG.debug("command processing complete");
            onComplete.continueProcessing();
            return;
        }

        // get the first processor
        IMessageProcessor processor = processingChain.get(0);

        // create a processing chain that no longer contains this processor
        final List<IMessageProcessor> newChain = processingChain.subList(1, processingChain.size());

        // let it process the event and pass its "next" processor: a method that
        // recursively calls this function with the current processor removed
        processor.processMessage(context, new IContinuationCallback() {

            @Override
            public void continueProcessing() throws MessageException {
            	processMessage(context, newChain, onComplete);
            }

        });

        LOG.debug("Leave processCommand");
    }

    @Override
    public void dispose() {
    }
}
