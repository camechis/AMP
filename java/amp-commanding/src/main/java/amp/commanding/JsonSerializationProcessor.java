package amp.commanding;


import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/14/13
 */
public class JsonSerializationProcessor implements ICommandProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(JsonSerializationProcessor.class);
    private static final String ENCODING = "UTF-8";


    private class DotNetTypeExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false; // don't skip any classes
        }

        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            return "$type".equals(arg0.getName()); // ignore fields named $type which may be added by the dotnet serializer
        }

    }


    private class GsonIgnoreExclusionStrategy implements ExclusionStrategy {
        private final Class<?> typeToSkip;

        private GsonIgnoreExclusionStrategy(Class<?> typeToSkip) {
            this.typeToSkip = typeToSkip;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(GsonIgnore.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return (clazz == typeToSkip);
        }
    }


    private Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setExclusionStrategies(
                    new DotNetTypeExclusionStrategy(),
                    new GsonIgnoreExclusionStrategy(GsonIgnore.class)
            ).create();


    @Override
    public void processCommand(CommandContext context, IContinuationCallback next) throws CommandException {

        if (CommandContext.Directions.In == context.getDirection()) {
    		this.processInbound(context, next);
    	}
    	else if (CommandContext.Directions.Out == context.getDirection()) {
    		this.processOutbound(context, next);
    	}
    }


    public void processInbound(CommandContext context, IContinuationCallback next) throws CommandException {

        Envelope env = context.getEnvelope();

        String eventType = env.getHeader(EnvelopeHeaderConstants.MESSAGE_TYPE);

        try {
            Class<?> type = Class.forName(eventType);
            String jsonString = new String(env.getPayload(), ENCODING);
            context.setCommand(this.gson.fromJson(jsonString, type));
        }
        catch (Exception ex) {
            String message = "Failed to deserialize an incoming command.";
            LOG.error(message, ex);
            throw new CommandException(message, ex);
        }

        next.continueProcessing();
    }

    public void processOutbound(CommandContext context, IContinuationCallback next) throws CommandException {

        Envelope env = context.getEnvelope();

        try {
            String jsonString = gson.toJson(context.getCommand());
            LOG.debug("Serialized event: " + jsonString);

            env.setPayload(jsonString.getBytes(ENCODING));

            next.continueProcessing();
        }
        catch (Exception ex) {
            String message = "Failed to serialize an outgoing command.";
            LOG.error(message, ex);
            throw new CommandException(message, ex);
        }
    }
}
