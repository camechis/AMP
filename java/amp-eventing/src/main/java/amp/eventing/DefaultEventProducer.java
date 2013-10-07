package amp.eventing;


import java.util.List;
import java.util.Map;

import amp.messaging.IMessageProcessor;
import amp.messaging.MessageException;
import amp.messaging.MessageSender;
import cmf.bus.IEnvelopeSender;
import cmf.eventing.IEventProducer;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultEventProducer extends MessageSender implements IEventProducer {

    public DefaultEventProducer(IEnvelopeSender envelopeSender) {
        super(envelopeSender);
    }

    public DefaultEventProducer(IEnvelopeSender envelopeSender, List<IMessageProcessor> processorChain) {
    	super(envelopeSender, processorChain);
    }


    @Override
    public void publish(Object event) throws MessageException {
    	super.send(event);
    }

    @Override
    public void publish(Object event, Map<String, String> headers) throws MessageException {
        super.send(event, headers);
    }
}
