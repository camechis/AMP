package amp.messaging;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import amp.bus.security.IUserInfoRepository;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/14/13
 */
public class OutboundHeadersProcessor implements IMessageProcessor {

	protected IUserInfoRepository userInfoRepo;
	
	
	public OutboundHeadersProcessor(IUserInfoRepository userInfoRepository) {
		this.userInfoRepo = userInfoRepository;
	}

	@Override
    public void processMessage(MessageContext context, IContinuationCallback next) throws MessageException {

        if (MessageContext.Directions.Out == context.getDirection()) {

            EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());

            UUID messageId = env.getMessageId();
            messageId = (null == messageId) ? UUID.randomUUID() : messageId;
            env.setMessageId(messageId);

            UUID correlationId = env.getCorrelationId();

            String messageType = env.getMessageType();
            messageType = StringUtils.isBlank(messageType) ? this.getMessageType(context.getMessage()) : messageType;
            env.setMessageType(messageType);

            String messageTopic = env.getMessageTopic();
            messageTopic = StringUtils.isBlank(messageTopic) ? this.getMessageTopic(context.getMessage()) : messageTopic;
            if (null != correlationId)
            {
                messageTopic = messageTopic + "#" + correlationId.toString();
            }
            env.setMessageTopic(messageTopic);

            env.setCreationTime(DateTime.now());

            String senderIdentity = env.getSenderIdentity();
            try {
				senderIdentity = StringUtils.isBlank(senderIdentity) ? userInfoRepo.getDistinguishedName(System.getProperty("user.name")).replaceAll(",", ", ") : senderIdentity;
			} catch (Exception ex) {
				throw new MessageException("Failed to get user identity from " + userInfoRepo.getClass().getCanonicalName(), ex);
			}
            env.setSenderIdentity(senderIdentity);
        }

        next.continueProcessing();
    }


    public String getMessageType(Object message) {

        String messageType = message.getClass().getCanonicalName();

        Message messageMetadata = message.getClass().getAnnotation(Message.class);

        if (null != messageMetadata) {
            messageType = messageMetadata.type();
        }

        return messageType;
    }

    public String getMessageTopic(Object message) {

        String messageTopic = message.getClass().getCanonicalName();

        Message messageMetadata = message.getClass().getAnnotation(Message.class);

        if (null != messageMetadata) {
            messageTopic = messageMetadata.topic();
        }

        return messageTopic;
    }
}
