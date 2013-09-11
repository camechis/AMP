package amp.messaging;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMessageProcessor {

    void processMessage(MessageContext context, IContinuationCallback onComplete) throws MessageException;
}
