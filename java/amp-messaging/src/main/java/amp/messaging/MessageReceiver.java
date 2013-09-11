package amp.messaging;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.messaging.MessageContext.Directions;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IEnvelopeReceiver;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class MessageReceiver implements IInboundProcessorCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    private IEnvelopeReceiver _envelopeReceiver;
    private IMessageProcessor _messageProcessor;

    public IMessageProcessor getMessageProcessor(){
    	return _messageProcessor;
    }
    
    public MessageReceiver(IEnvelopeReceiver envelopeReceiver) {
        _envelopeReceiver = envelopeReceiver;
    }

    public MessageReceiver(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processorChain) {
        this(envelopeReceiver, new MessageProcessorChain(processorChain));
    }

    public MessageReceiver(IEnvelopeReceiver envelopeReceiver, IMessageProcessor messageProcessor) {
        this(envelopeReceiver);
        _messageProcessor = messageProcessor;
    }

    public <TMESSAGE> void onMessageReceived(IMessageHandler<TMESSAGE> handler, 
    		IEnvelopeFilterPredicate predicate) throws MessageException, IllegalArgumentException {

        LOG.debug("Enter onMessageReceived");
        if (null == handler) { throw new IllegalArgumentException("Cannot register a null handler"); }


        // create a registration object
        final MessageRegistration registration = new MessageRegistration(_messageProcessor, handler, predicate);

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

    @Override
    //TODO: This code is duplicative of code in MessageRegistration
    public Object ProcessInbound(Envelope envelope) throws Exception {
        final MessageContext context = new MessageContext(Directions.In, envelope);

        this._messageProcessor.processMessage(
                context,
                new IContinuationCallback() {

                    @Override
                    public void continueProcessing() {
                        LOG.info("Completed inbound processing - returning event");
                    }
                });

        return context.getMessage();
    }

    public void dispose() {
        _envelopeReceiver.dispose();
    }
}
