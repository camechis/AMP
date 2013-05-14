package amp.commanding;

import cmf.bus.IDisposable;
import cmf.bus.IRegistration;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public interface ICommandReceiver extends IDisposable {

    <TCOMMAND> void onCommandReceived(ICommandHandler<TCOMMAND> handler) throws CommandException, IllegalArgumentException;
}
