package amp.commanding;


import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public interface ICommandHandler<TCOMMAND> {

    Class<TCOMMAND> getCommandType();

    void handle(TCOMMAND command, Map<String, String> headers);
}
