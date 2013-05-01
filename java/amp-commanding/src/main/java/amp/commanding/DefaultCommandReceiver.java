package amp.commanding;


import java.util.List;
import javax.annotation.Nullable;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeReceiver;
import cmf.bus.IRegistration;
import com.google.common.base.Function;
import com.google.common.base.Functions;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class DefaultCommandReceiver implements ICommandReceiver {

    private IEnvelopeReceiver _envelopeReceiver;
    private List<ICommandProcessor> _processingChain;


    public List<ICommandProcessor> getProcessingChain() { return _processingChain; }
    public void setProcessingChain(List<ICommandProcessor> value) { _processingChain = value; }


    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver) {
        _envelopeReceiver = envelopeReceiver;
    }

    public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver, List<ICommandProcessor> processingChain) {
        _envelopeReceiver = envelopeReceiver;
        _processingChain = processingChain;
    }


    @Override
    public <TCOMMAND> void onCommandReceived(ICommandHandler<TCOMMAND> handler) throws CommandException {

        CommandRegistration registration = new CommandRegistration(handler, new Function<Envelope, Object>() {

            @Override
            public Object apply(@Nullable Envelope envelope) {

                try {

                    CommandContext ctx = new CommandContext(CommandContext.Directions.In, envelope);
                    
                }
                catch (Exception ex) {

                }

                return null;
            }
        });
    }
}
