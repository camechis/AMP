package amp.messaging;


import java.util.List;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class MessageSender implements IMessageChainProcessor {

    static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);

    private IEnvelopeSender _envelopeSender;
    private List<IMessageProcessor> _processorChain;


    public MessageSender(IEnvelopeSender envelopeSender) {
        _envelopeSender = envelopeSender;
    }

    public MessageSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processorChain) {
        _envelopeSender = envelopeSender;
        _processorChain = processorChain;
    }


    public void send(Object message) throws MessageException {

        if (null == message) { throw new IllegalArgumentException("Cannot send a null message."); }
        LOG.debug("Enter send");

        final Envelope envelope = new Envelope();
        final MessageContext context = new MessageContext(MessageContext.Directions.Out, message, envelope);

        try {

            this.processMessage(context, _processorChain, new IContinuationCallback() {

                @Override
                public void continueProcessing() throws MessageException {

                    try {
                        _envelopeSender.send(context.getEnvelope());
                    }
                    catch (Exception ex) {
                        String error = "Failed to send envelope containing message.";
                        LOG.error(error, ex);
                        throw new MessageException(error, ex);
                    }
                }
            });
        }
        catch(Exception ex) {
            String warning = "Caught an exception while processing message.";
            LOG.warn(warning, ex);
            throw new MessageException(warning, ex);
        }

        LOG.debug("Leave send");
    }

    @Override
    public void processMessage(
            final MessageContext context,
            final List<IMessageProcessor> processingChain,
            final IContinuationCallback onComplete) throws MessageException {

        LOG.debug("Enter processMessage");

        // if the chain is null or empty, complete processing
        if ( (null == processingChain) || (0 == processingChain.size()) ) {
            LOG.debug("message processing complete");
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

        LOG.debug("Leave processMessage");
    }

    public void dispose() {
    }
}
