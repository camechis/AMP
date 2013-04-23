package amp.topology.core.factory.dynamic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import amp.bus.rabbit.topology.Queue;
import amp.topology.core.factory.dynamic.expeval.MvelExpressionEvaluator;
import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.definitions.QueueDefinition;

@RunWith(MockitoJUnitRunner.class)
public class DynamicQueueFactoryTest {

	@Mock DefinitionRepository<QueueDefinition> definitionRepo;
	
	
	@Test
	public void test() {
		
		// TODO: Turn into proper mock after figuring out the generics
		// problem with Mockito.
		ExpressionEvaluator evaluator = new MvelExpressionEvaluator();
		
		QueueDefinition definition = 
			new QueueDefinition(
				"id", "description", "my-queue", "true", "true", 
				"true", "/", "true", new HashMap<String, String>());
		
		when(definitionRepo.get(any(String.class))).thenReturn(definition);
		
		DynamicQueueFactory factory = new DynamicQueueFactory(evaluator, definitionRepo);
		
		Queue queue = factory.build("fake context");
		
		assertEquals("my-queue", queue.getName());
		assertEquals("/", queue.getVirtualHost());
		assertEquals(true, queue.isAutoDelete());
		assertEquals(true, queue.isDurable());
		assertEquals(true, queue.isExclusive());
		assertEquals(true, queue.shouldDeclare());
	}

}
