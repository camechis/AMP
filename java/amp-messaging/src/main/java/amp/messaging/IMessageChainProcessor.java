package amp.messaging;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/7/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMessageChainProcessor {

    public void processMessage(
            final MessageContext context,
            final List<IMessageProcessor> processingChain,
            final IContinuationCallback onComplete)
        throws MessageException;
}
