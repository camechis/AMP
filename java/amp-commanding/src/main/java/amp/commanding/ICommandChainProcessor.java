package amp.commanding;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/7/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICommandChainProcessor {

    public void processCommand(
            final CommandContext context,
            final List<ICommandProcessor> processingChain,
            final IContinuationCallback onComplete)
        throws CommandException;
}
