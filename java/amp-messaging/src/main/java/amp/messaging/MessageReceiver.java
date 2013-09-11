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
public class MessageReceiver {

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

    public <TMESSAGE> void onMessageReceived(IMessageHandler<TMESSAGE> handler) throws MessageException, IllegalArgumentException {

        LOG.debug("Enter onMessageReceived");
        if (null == handler) { throw new IllegalArgumentException("Cannot register a null handler"); }


        // create a registration object
        final MessageRegistration registration = new MessageRegistration(_messageProcessor, handler);

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

    public void dispose() {
        _envelopeReceiver.dispose();
    }
}
