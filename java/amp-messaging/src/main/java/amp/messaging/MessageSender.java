package amp.messaging;


import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class MessageSender {

    static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);

    private IEnvelopeSender _envelopeSender;
    private IMessageProcessor _messageProcessor ;

    public IMessageProcessor getMessageProcessor(){
    	return _messageProcessor;
    }

    public MessageSender(IEnvelopeSender envelopeSender) {
        _envelopeSender = envelopeSender;
    }

    public MessageSender(IEnvelopeSender envelopeSender, IMessageProcessor messageProcessor) {
        this(envelopeSender);
        _messageProcessor = messageProcessor;
    }

    public MessageSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processorChain) {
        this(envelopeSender, new MessageProcessorChain(processorChain));
    }


    public void send(Object message) throws MessageException {

        this.send(message, null);
    }

    public void send(Object message, Map<String, String> headers) throws MessageException {

        if (null == message) { throw new IllegalArgumentException("Cannot send a null message."); }
        LOG.debug("Enter send");

        final Envelope envelope = new Envelope();
        final MessageContext context = new MessageContext(MessageContext.Directions.Out, envelope, message);

        // headers may be null or empty
        if (null != headers) { envelope.setHeaders(headers); }

        try {

            _messageProcessor.processMessage(context, new IContinuationCallback() {

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

    public void dispose() {
    }
}
