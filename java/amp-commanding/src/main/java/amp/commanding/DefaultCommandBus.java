package amp.commanding;

import java.util.List;

import cmf.bus.IEnvelopeBus;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageException;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultCommandBus implements ICommandBus {

    private ICommandSender _sender;
    private ICommandReceiver _receiver;


    public DefaultCommandBus(IEnvelopeBus envelopeBus, 
    		List<IMessageProcessor> inboundProcessors,
    		List<IMessageProcessor> outboundProcessors) {
        _sender = new DefaultCommandSender(envelopeBus, outboundProcessors);
        _receiver = new DefaultCommandReceiver(envelopeBus, inboundProcessors);
    }


    @Override
    public void onCommandReceived(ICommandHandler handler) throws MessageException, IllegalArgumentException {
        _receiver.onCommandReceived(handler);
    }

    @Override
    public void send(Object command) throws MessageException {
        _sender.send(command);
    }

    @Override
    public void dispose() {
        _receiver.dispose();
    }
}
