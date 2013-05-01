package amp.commanding;

import cmf.bus.IRegistration;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICommandReceiver {

    <TCOMMAND> void onCommandReceived(ICommandHandler<TCOMMAND> handler) throws CommandException;
}
