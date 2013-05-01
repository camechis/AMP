package amp.commanding;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICommandProcessor {

    void processCommand(CommandContext context, IContinuationCallback next);
}
