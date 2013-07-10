package amp.commanding;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IContinuationCallback {

    public void continueProcessing() throws CommandException;
}
