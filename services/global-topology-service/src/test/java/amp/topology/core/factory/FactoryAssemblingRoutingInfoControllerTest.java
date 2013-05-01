package amp.topology.core.factory;

import static amp.topology.test.Utils.getClient;
import static amp.topology.test.Utils.getRoutingContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.InOrder;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.index.RoutingInfoIndex;
import amp.topology.core.factory.index.RoutingInfoSelectionContext;
import amp.topology.core.factory.index.selection.RoutingInfoSelectionStrategy;
import amp.topology.core.model.Client;


public class FactoryAssemblingRoutingInfoControllerTest {
	
	@Test
	public void controller_calls_components_in_the_correct_sequence() {
		
		RoutingInfo expectedRoutingInfo = mock(RoutingInfo.class);
		
		FactoryReference<RoutingInfo> routingInfoReference = 
			new FactoryReference<RoutingInfo>("factory", "context");
		
		RoutingInfoSelectionContext selectionContext = 
				mockSelectionContext(routingInfoReference);
		
		List<RoutingInfoSelectionContext> selections = Arrays.asList(selectionContext);
		
		Map<String, String> context = getRoutingContext("test.Topic", "pubsub");
		
		Client client = getClient("Bob", "User", "Admin");
		
		RoutingInfoIndex routingInfoIndex = 
				mockRoutingInfoIndex(client, context, selections);
		
		RoutingInfoSelectionStrategy selectionStrategy = 
				mockRoutingInfoSelectionStrategy(selections, routingInfoReference);
				
		DelegatingModelFactory modelFactory = 
				mockDelegatingModelFactory(routingInfoReference, expectedRoutingInfo);
		
		FactoryAssemblingRoutingInfoController controller = 
				new FactoryAssemblingRoutingInfoController(
					routingInfoIndex, selectionStrategy, modelFactory);
		
		RoutingInfo actualRoutingInfo = controller.getRouteFromContext(client, context);
		
		InOrder inOrder = inOrder(routingInfoIndex, selectionStrategy, modelFactory);
		
		inOrder.verify(routingInfoIndex).getMatches(client, context);
		inOrder.verify(selectionStrategy).selectBestMatch(selections);
		inOrder.verify(modelFactory).make(routingInfoReference);
		
		assertEquals(expectedRoutingInfo, actualRoutingInfo);
	}

	@Test
	public void controller_returns_null_when_selection_strategy_returns_null(){
		
		FactoryReference<RoutingInfo> routingInfoReference = 
			new FactoryReference<RoutingInfo>("factory", "context");
		
		RoutingInfoSelectionContext selectionContext = 
				mockSelectionContext(null);
		
		List<RoutingInfoSelectionContext> selections = Arrays.asList(selectionContext);
		
		Map<String, String> context = getRoutingContext("test.Topic", "pubsub");
		
		Client client = getClient("Bob", "User", "Admin");
		
		RoutingInfoIndex routingInfoIndex = 
				mockRoutingInfoIndex(client, context, selections);
		
		RoutingInfoSelectionStrategy selectionStrategy = 
				mockRoutingInfoSelectionStrategy(selections, routingInfoReference);
				
		DelegatingModelFactory modelFactory = 
				mockDelegatingModelFactory(routingInfoReference, null);
		
		FactoryAssemblingRoutingInfoController controller = 
				new FactoryAssemblingRoutingInfoController(
					routingInfoIndex, selectionStrategy, modelFactory);
		
		RoutingInfo actualRoutingInfo = controller.getRouteFromContext(client, context);
		
		assertNull(actualRoutingInfo);
	}
	
	@Test
	public void controller_returns_null_when_model_factory_returns_null(){
		
		FactoryReference<RoutingInfo> routingInfoReference = 
			new FactoryReference<RoutingInfo>("factory", "context");
		
		RoutingInfoSelectionContext selectionContext = 
				mockSelectionContext(null);
		
		List<RoutingInfoSelectionContext> selections = Arrays.asList(selectionContext);
		
		Map<String, String> context = getRoutingContext("test.Topic", "pubsub");
		
		Client client = getClient("Bob", "User", "Admin");
		
		RoutingInfoIndex routingInfoIndex = 
				mockRoutingInfoIndex(client, context, selections);
		
		RoutingInfoSelectionStrategy selectionStrategy = 
				mockRoutingInfoSelectionStrategy(selections, routingInfoReference);
				
		DelegatingModelFactory modelFactory = 
				mock(DelegatingModelFactory.class);
		
		FactoryAssemblingRoutingInfoController controller = 
				new FactoryAssemblingRoutingInfoController(
					routingInfoIndex, selectionStrategy, modelFactory);
		
		RoutingInfo actualRoutingInfo = controller.getRouteFromContext(client, context);
		
		assertNull(actualRoutingInfo);
	}
	
	@Test
	public void controller_returns_null_when_index_returns_null(){
		
		Map<String, String> context = getRoutingContext("test.Topic", "pubsub");
		
		Client client = getClient("Bob", "User", "Admin");
		
		RoutingInfoIndex routingInfoIndex = 
				mockRoutingInfoIndex(null, null, null);
		
		RoutingInfoSelectionStrategy selectionStrategy = 
				mock(RoutingInfoSelectionStrategy.class);
				
		DelegatingModelFactory modelFactory = 
				mock(DelegatingModelFactory.class);
		
		FactoryAssemblingRoutingInfoController controller = 
				new FactoryAssemblingRoutingInfoController(
					routingInfoIndex, selectionStrategy, modelFactory);
		
		RoutingInfo actualRoutingInfo = controller.getRouteFromContext(client, context);
		
		assertNull(actualRoutingInfo);
	}
	
	protected RoutingInfoSelectionContext mockSelectionContext(
			FactoryReference<RoutingInfo> routingInfoReference){
		
		RoutingInfoSelectionContext selectionContext = 
				mock(RoutingInfoSelectionContext.class);
		
		when(selectionContext.getRoutingInfo()).thenReturn(routingInfoReference);
		
		return selectionContext;
	}
	
	protected RoutingInfoIndex mockRoutingInfoIndex(
			Client client, Map<String, String> context, 
			List<RoutingInfoSelectionContext> selections){
		
		RoutingInfoIndex routingInfoIndex = mock(RoutingInfoIndex.class);
		
		when(routingInfoIndex.getMatches(client, context)).thenReturn(selections);
		
		return routingInfoIndex;
	}
	
	protected RoutingInfoSelectionStrategy mockRoutingInfoSelectionStrategy(
			List<RoutingInfoSelectionContext> selections,
			FactoryReference<RoutingInfo> routingInfoReference){
		
		RoutingInfoSelectionStrategy selectionStrategy = 
				mock(RoutingInfoSelectionStrategy.class);
			
		when(selectionStrategy.selectBestMatch(selections))
			.thenReturn(routingInfoReference);
		
		return selectionStrategy;
	}
	
	protected DelegatingModelFactory mockDelegatingModelFactory(
			FactoryReference<RoutingInfo> routingInfoReference,
			RoutingInfo expectedRoutingInfo){
	
		DelegatingModelFactory modelFactory = mock(DelegatingModelFactory.class);
		
		when(modelFactory.make(routingInfoReference)).thenReturn(expectedRoutingInfo);
		
		return modelFactory;
	}
}
