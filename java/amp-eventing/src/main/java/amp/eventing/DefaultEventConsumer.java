package amp.eventing;


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
import cmf.eventing.IEventConsumer;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultEventConsumer implements IEventConsumer, IMessageChainProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultEventConsumer.class);

    private IEnvelopeReceiver _envelopeReceiver;
    private List<IMessageProcessor> _processingChain;


    public List<IMessageProcessor> getProcessingChain() { return _processingChain; }
    public void setProcessingChain(List<IMessageProcessor> value) { _processingChain = value; }


    public DefaultEventConsumer(IEnvelopeReceiver envelopeReceiver) {
        _envelopeReceiver = envelopeReceiver;
    }

    public DefaultEventConsumer(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processorChain) {
        _envelopeReceiver = envelopeReceiver;
        _processingChain = processorChain;
    }

    @Override
    public <TEVENT> void subscribe(IEventHandler<TEVENT> handler) throws MessageException, IllegalArgumentException {
        Class<TEVENT> type = handler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(handler, filterPredicate);
    }
    
    @Override
	public <TEVENT> void subscribe(IEventHandler<TEVENT> handler,
			IEventFilterPredicate predicate) throws MessageException {

        LOG.debug("Enter onEventReceived");
        if (null == handler) { throw new IllegalArgumentException("Cannot register a null handler"); }


        // create a registration object
        final MessageRegistration registration = new MessageRegistration(
        		this, _processingChain, new EventMessageHandler<TEVENT>(handler));

        // and register it with the envelope receiver
        try {
            _envelopeReceiver.register(registration);
        }
        catch (Exception ex) {
            String message = "Failed to register for a event";
            LOG.error(message, ex);
            throw new MessageException(message, ex);
        }


        LOG.debug("Leave onEventReceived");
    }

    public void processMessage(
            final MessageContext context,
            final IContinuationCallback onComplete) throws MessageException {
    	this.processMessage(context, _processingChain, onComplete);
    }
    
    @Override
    public void processMessage(
            final MessageContext context,
            final List<IMessageProcessor> processingChain,
            final IContinuationCallback onComplete) throws MessageException {

        LOG.debug("Enter processEvent");

        // if the chain is null or empty, complete processing
        if ( (null == processingChain) || (0 == processingChain.size()) ) {
            LOG.debug("event processing complete");
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

        LOG.debug("Leave processEvent");
    }

    public void dispose() {
        _envelopeReceiver.dispose();
    }
    
    private static class EventMessageHandler<TEVENT> implements IMessageHandler<TEVENT>{
    	private static final Logger LOG = LoggerFactory.getLogger(EventMessageHandler.class);

        private final IEventHandler<TEVENT> _handler;
  
        public EventMessageHandler(IEventHandler<TEVENT> handler){
            _handler = handler;
        }

        public Class<TEVENT> getMessageType(){
            return _handler.getEventType(); 
        }

        @Override
		public Object handle(TEVENT message, Map<String, String> headers){
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
