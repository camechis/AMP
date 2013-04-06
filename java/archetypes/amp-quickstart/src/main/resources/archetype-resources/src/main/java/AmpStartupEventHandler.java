package ${package};

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.eventing.IEventHandler;

public class AmpStartupEventHandler implements IEventHandler<AmpStartupEvent> {

	private static final Logger logger = LoggerFactory.getLogger(AmpStartupEventHandler.class);
	
	@Override
	public Class<AmpStartupEvent> getEventType() {
		
		return AmpStartupEvent.class;
	}
	
	@Override
	public Object handle(AmpStartupEvent event, Map<String, String> context) {
		
		logger.info("Amp startup event created at: {}", event.getStartTime());
		
		return null;
	}

	@Override
	public Object handleFailed(Envelope envelope, Exception ex) {
		
		return null;
	}

}
