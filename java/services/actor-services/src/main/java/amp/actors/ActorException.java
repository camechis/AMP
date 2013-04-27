package amp.actors;

/**
 * Created with IntelliJ IDEA.
 * User: jruiz
 * Date: 4/26/13
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActorException extends Exception {

    public ActorException() {}

    public ActorException(String message) { super(message); }

    public ActorException(String message, Throwable cause) { super(message, cause); }

    public ActorException(Throwable cause) { super(cause); }
}
