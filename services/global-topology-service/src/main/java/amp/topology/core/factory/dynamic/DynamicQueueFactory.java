package amp.topology.core.factory.dynamic;

import java.util.Map;

import amp.bus.rabbit.topology.Queue;
import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.definitions.QueueDefinition;

public class DynamicQueueFactory extends EvaluatingChainedTopologyFactory<Queue> {

	DefinitionRepository<QueueDefinition> repository;
	
	public DynamicQueueFactory(ExpressionEvaluator evaluator, DefinitionRepository<QueueDefinition> repository) {
		
		super(evaluator);
		
		this.repository = repository;
	}

	@Override
	public Queue build(String context) {

		QueueDefinition definition = this.repository.get(context);
		
		String name = evaluate(definition.getNameExpression(), String.class);
		
		String vhost = evaluate(definition.getVirtualHostExpression(), String.class);
		
		boolean isAutoDelete = 
				evaluate(definition.getIsAutoDeleteExpression(), Boolean.class);
		
		boolean isDurable = 
				evaluate(definition.getIsDurableExpression(), Boolean.class);
		
		boolean shouldDeclare = 
				evaluate(definition.getShouldDeclareExpression(), Boolean.class);
		
		boolean isExclusive = 
				evaluate(definition.getIsExclusiveExpression(), Boolean.class);
		
		Map<String, Object> arguments = 
				evaluateArgumentExpressions(definition.getArgumentExpressions());
		
		return Queue.builder()
				.name(name)
				.vhost(vhost)
				.isAutoDelete(isAutoDelete)
				.isDurable(isDurable)
				.isExclusive(isExclusive)
				.declare(shouldDeclare)
				.arguments(arguments)
				.build();
	}

}
