package amp.commanding;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/14/13
 */
public class OutboundHeadersProcessor implements ICommandProcessor {

    @Override
    public void processCommand(CommandContext context, IContinuationCallback next) throws CommandException {

        if (CommandContext.Directions.Out == context.getDirection()) {

            EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());

            UUID messageId = env.getMessageId();
            messageId = (null == messageId) ? UUID.randomUUID() : messageId;
            env.setMessageId(messageId);

            UUID correlationId = env.getCorrelationId();

            String messageType = env.getMessageType();
            messageType = StringUtils.isBlank(messageType) ? this.getMessageType(context.getCommand()) : messageType;
            env.setMessageType(messageType);

            String messageTopic = env.getMessageTopic();
            messageTopic = StringUtils.isBlank(messageTopic) ? this.getMessageTopic(context.getCommand()) : messageTopic;
            if (null != correlationId)
            {
                messageTopic = messageTopic + "#" + correlationId.toString();
            }
            env.setMessageTopic(messageTopic);

            env.setCreationTime(DateTime.now());

            String senderIdentity = env.getSenderIdentity();
            senderIdentity = StringUtils.isBlank(senderIdentity) ? System.getProperty("user.name") : senderIdentity;
            env.setSenderIdentity(senderIdentity);

            next.continueProcessing();
        }
        else {
            next.continueProcessing();
        }
    }


    public String getMessageType(Object command) {

        String messageType = command.getClass().getCanonicalName();

        Command commandMetadata = command.getClass().getAnnotation(Command.class);

        if (null != commandMetadata) {
            messageType = commandMetadata.type();
        }

        return messageType;
    }

    public String getMessageTopic(Object command) {

        String messageTopic = command.getClass().getCanonicalName();

        Command commandMetadata = command.getClass().getAnnotation(Command.class);

        if (null != commandMetadata) {
            messageTopic = commandMetadata.topic();
        }

        return messageTopic;
    }
}
