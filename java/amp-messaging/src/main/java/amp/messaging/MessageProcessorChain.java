package amp.messaging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An IMessageProcessor implementation used to join multiple other IMessageProcessors
 * in an ordered chain, so that the entire set can be treated as a single processor
 * for referencing and processing purposes.
 * 
 * Use of this class eliminates the need to have chaining logic in more than 
 * one location and also permits the same chain to be declared once and used multiply.
 */
public class MessageProcessorChain implements IMessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorChain.class);

    private List<IMessageProcessor> _processingChain;
	
    public MessageProcessorChain(List<IMessageProcessor> processorChain) {
        _processingChain = processorChain;
    }
    
	@Override
	public void processMessage(MessageContext context, IContinuationCallback onComplete) throws MessageException {
        LOG.debug("Enter processMessage - Direction: " + context.getDirection().toString());
		processMessage(context,_processingChain, onComplete);
        LOG.debug("Leave processMessage - Direction: " + context.getDirection().toString());
	}
	
    private void processMessage(
            final MessageContext context,
            final List<IMessageProcessor> processingChain,
            final IContinuationCallback onComplete) throws MessageException {


        // if the chain is null or empty, complete processing
        if ( (null == processingChain) || (0 == processingChain.size()) ) {
            LOG.debug("Message processing complete. Direction: " + context.getDirection().toString());
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
    }
}
