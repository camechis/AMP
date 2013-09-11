package amp.messaging;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.IEnvelopeReceiver;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class MessageReceiver implements IMessageChainProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    private IEnvelopeReceiver _envelopeReceiver;
    private List<IMessageProcessor> _processingChain;


    public List<IMessageProcessor> getProcessingChain() { return _processingChain; }
    public void setProcessingChain(List<IMessageProcessor> value) { _processingChain = value; }


    public MessageReceiver(IEnvelopeReceiver envelopeReceiver) {
        _envelopeReceiver = envelopeReceiver;
    }

    public MessageReceiver(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processorChain) {
        _envelopeReceiver = envelopeReceiver;
        _processingChain = processorChain;
    }


    public <TMESSAGE> void onMessageReceived(IMessageHandler<TMESSAGE> handler) throws MessageException, IllegalArgumentException {

        LOG.debug("Enter onMessageReceived");
        if (null == handler) { throw new IllegalArgumentException("Cannot register a null handler"); }


        // create a registration object
        final MessageRegistration registration = new MessageRegistration(this, _processingChain, handler);

        // and register it with the envelope receiver
        try {
            _envelopeReceiver.register(registration);
        }
        catch (Exception ex) {
            String message = "Failed to register for a message";
            LOG.error(message, ex);
            throw new MessageException(message, ex);
        }


        LOG.debug("Leave onMessageReceived");
    }

    public void processMessage(
            final MessageContext context,
            final IContinuationCallback onComplete) throws MessageException {
    	processMessage(context,_processingChain, onComplete);
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
        _envelopeReceiver.dispose();
    }
}
