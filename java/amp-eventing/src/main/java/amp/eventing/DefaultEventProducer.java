package amp.eventing;


import java.util.List;

import amp.messaging.IContinuationCallback;
import amp.messaging.IMessageChainProcessor;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageContext;
import amp.messaging.MessageException;
import amp.messaging.MessageSender;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeSender;
import cmf.eventing.IEventProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultEventProducer extends MessageSender implements IEventProducer, IMessageChainProcessor {

    public DefaultEventProducer(IEnvelopeSender envelopeSender) {
        super(envelopeSender);
    }

    public DefaultEventProducer(IEnvelopeSender envelopeSender, List<IMessageProcessor> processorChain) {
    	super(envelopeSender, processorChain);
    }


    @Override
    public void publish(Object event) throws MessageException {
    	send(event);
    }
}
