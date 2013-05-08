package amp.gel.dao.impl.derby.data.processors;

import amp.eventing.IEventProcessor;

public interface EventSequenceProcessor extends IEventProcessor {
	void restartEventSequence() throws Exception;
}
