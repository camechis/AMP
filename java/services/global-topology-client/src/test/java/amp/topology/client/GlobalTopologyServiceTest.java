package amp.topology.client;

import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;

import org.junit.Test;

public class GlobalTopologyServiceTest {

	@Test(expected=RoutingInfoNotFoundException.class)
	public void gts_fails_when_it_cant_find_a_route_for_a_topic() {
		
		IRoutingInfoRetriever retriever = mock(IRoutingInfoRetriever.class);
		
		GlobalTopologyService gts = new GlobalTopologyService(retriever);
		
		gts.getRoutingInfo(TestUtils.buildRoutingHints("A nonexistent topic!"));
	}

}
