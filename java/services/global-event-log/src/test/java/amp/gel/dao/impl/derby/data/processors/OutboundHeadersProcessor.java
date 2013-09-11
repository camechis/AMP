package amp.gel.dao.impl.derby.data.processors;

import java.util.HashMap;

import amp.bus.security.IUserInfoRepository;
import amp.bus.security.InMemoryUserInfoRepository;

public class OutboundHeadersProcessor extends
		amp.messaging.OutboundHeadersProcessor implements EventSequenceProcessor {

	public OutboundHeadersProcessor() {
		this(new InMemoryUserInfoRepository(new HashMap<String, String>()));
	}

	public OutboundHeadersProcessor(IUserInfoRepository userInfoRepository) {
		//super(userInfoRepository);
	}

	public void restartEventSequence() {
		// do nothing
	}
}
