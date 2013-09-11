package amp.messaging;

import cmf.bus.IEnvelopeFilterPredicate;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBus {

    private MessageSender _sender;
    private MessageReceiver _receiver;


    public MessageBus(MessageSender sender, MessageReceiver receiver) {
        _sender = sender;
        _receiver = receiver;
    }


    public void onMessageReceived(IMessageHandler handler, IEnvelopeFilterPredicate filterPredicate) throws MessageException, IllegalArgumentException {
        _receiver.onMessageReceived(handler, filterPredicate);
    }

    public void send(Object message) throws MessageException {
        _sender.send(message);
    }

    public void dispose() {
        _receiver.dispose();
    }
}
