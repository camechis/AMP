package ${package};

import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmpStartupEventHandlerTest {

	private static final Logger logger = LoggerFactory.getLogger(AmpStartupEventHandlerTest.class);
	
	@Test
	public void handler_calls_to_string_on_event_confirming_it_was_logged() {
		
		logger.debug("String is called!");
		
		AmpStartupEvent event = spy(new AmpStartupEvent());
		
		AmpStartupEventHandler handler = new AmpStartupEventHandler();
		
		handler.handle(event, new HashMap<String, String>());
		
		verify(event).getStartTime();
	}

}