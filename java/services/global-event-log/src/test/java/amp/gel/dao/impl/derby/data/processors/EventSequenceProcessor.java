package amp.gel.dao.impl.derby.data.processors;

import amp.messaging.IMessageProcessor;

public interface EventSequenceProcessor extends IMessageProcessor {
	void restartEventSequence() throws Exception;
}
