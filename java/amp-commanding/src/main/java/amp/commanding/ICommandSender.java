package amp.commanding;

import amp.messaging.MessageException;
import cmf.bus.IDisposable;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public interface ICommandSender extends IDisposable {

    void send(Object command) throws MessageException;
}
