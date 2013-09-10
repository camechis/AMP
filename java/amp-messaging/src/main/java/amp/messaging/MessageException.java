package amp.messaging;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageException extends Throwable {

    public MessageException() { }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
