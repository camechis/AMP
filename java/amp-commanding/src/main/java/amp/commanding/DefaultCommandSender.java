package amp.commanding;


import java.util.List;

import amp.messaging.IMessageProcessor;
import amp.messaging.MessageSender;
import cmf.bus.IEnvelopeSender;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandSender extends MessageSender implements ICommandSender {

    public DefaultCommandSender(IEnvelopeSender envelopeSender) {
        super(envelopeSender);
    }

    public DefaultCommandSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processorChain) {
        super(envelopeSender, processorChain);
    }
}
