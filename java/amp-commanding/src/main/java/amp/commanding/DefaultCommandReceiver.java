package amp.commanding;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.messaging.IContinuationCallback;
import amp.messaging.IMessageChainProcessor;
import amp.messaging.IMessageHandler;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageContext;
import amp.messaging.MessageException;
import amp.messaging.MessageRegistration;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeReceiver;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandReceiver implements ICommandReceiver, IMessageChainProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCommandReceiver.class);

    private IEnvelopeReceiver _envelopeReceiver;
    private List<IMessageProcessor> _processingChain;


    public List<IMessageProcessor> getProcessingChain() { return _processingChain; }
    public void setProcessingChain(List<IMessageProcessor> value) { _processingChain = value; }


    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver) {
        _envelopeReceiver = envelopeReceiver;
    }

    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processorChain) {
        _envelopeReceiver = envelopeReceiver;
        _processingChain = processorChain;
    }


    @Override
    public <TCOMMAND> void onCommandReceived(ICommandHandler<TCOMMAND> handler) throws MessageException, IllegalArgumentException {

        LOG.debug("Enter onCommandReceived");
        if (null == handler) { throw new IllegalArgumentException("Cannot register a null handler"); }


        // create a registration object
        final MessageRegistration registration = new MessageRegistration(
        		this, _processingChain, new CommandMessageHandler<TCOMMAND>(handler));

        // and register it with the envelope receiver
        try {
            _envelopeReceiver.register(registration);
        }
        catch (Exception ex) {
            String message = "Failed to register for a command";
            LOG.error(message, ex);
            throw new MessageException(message, ex);
        }


        LOG.debug("Leave onCommandReceived");
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
        _envelopeReceiver.dispose();
    }
    
    private static class CommandMessageHandler<TCOMMAND> implements IMessageHandler<TCOMMAND>{
    	private static final Logger LOG = LoggerFactory.getLogger(CommandMessageHandler.class);

        private final ICommandHandler<TCOMMAND> _handler;
  
        public CommandMessageHandler(ICommandHandler<TCOMMAND> handler){
            _handler = handler;
        }

        public Class<TCOMMAND> getMessageType(){
            return _handler.getCommandType(); 
        }

        @Override
		public Object handle(TCOMMAND message, Map<String, String> headers){
            _handler.handle(message, headers);
            return null;
        }

        @Override
        public Object handleFailed(Envelope env, Exception ex){
        	LOG.warn("Failed to handle envelope.", ex);
            return null;
        }
    }
}
