package amp.commanding;

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


    public DefaultCommandBus(ICommandSender sender, ICommandReceiver receiver) {
        _sender = sender;
        _receiver = receiver;
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
