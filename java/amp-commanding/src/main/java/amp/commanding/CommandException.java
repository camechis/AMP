package amp.commanding;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandException extends Throwable {

    public CommandException() { }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
