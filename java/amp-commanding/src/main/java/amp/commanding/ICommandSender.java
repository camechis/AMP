package amp.commanding;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICommandSender {

    void send(Object command) throws CommandException;
}
