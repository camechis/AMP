package amp.eventing.streaming;

import amp.messaging.EnvelopeHelper;
import amp.messaging.IContinuationCallback;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageContext;
import amp.messaging.MessageException;

public class StreamingHeadersProcessor implements IMessageProcessor {
    @Override
    public void processMessage(MessageContext context, IContinuationCallback continuation) throws MessageException  {
        final String endOfStreamTopic = EndOfStream.class.getCanonicalName();
        final String collectionSizeTopic = CollectionSizeNotifier.class.getCanonicalName();

        EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());

        if (context.getDirection().equals(MessageContext.Directions.Out)) {

            if (env.getMessageTopic().equals(endOfStreamTopic)) {
                env.setMessageTopic(((EndOfStream)context.getMessage()).getStreamType());
            }

            if (env.getMessageTopic().equals(collectionSizeTopic)) {
                env.setMessageTopic(((CollectionSizeNotifier)context.getMessage()).getCollectionType());
            }
        }

        continuation.continueProcessing();
    }
}
