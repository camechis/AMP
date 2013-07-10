package amp.extensions.commons.builder;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/13/13
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuilderException extends Throwable {

    public BuilderException() { }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }
}
