package amp.commanding;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.messaging.IMessageHandler;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageException;
import amp.messaging.MessageReceiver;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeReceiver;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandReceiver extends MessageReceiver implements ICommandReceiver {

    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver) {
        super(envelopeReceiver);
    }

    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processorChain) {
        super(envelopeReceiver, processorChain);
    }

    @Override
    public <TCOMMAND> void onCommandReceived(ICommandHandler<TCOMMAND> handler) throws MessageException, IllegalArgumentException {
    	onMessageReceived(new CommandMessageHandler<TCOMMAND>(handler));
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
