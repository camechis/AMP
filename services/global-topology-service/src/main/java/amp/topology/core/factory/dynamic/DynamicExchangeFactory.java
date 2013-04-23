package amp.topology.core.factory.dynamic;

import java.util.Map;

import amp.bus.rabbit.topology.Exchange;
import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.definitions.ExchangeDefinition;

public class DynamicExchangeFactory extends EvaluatingChainedTopologyFactory<Exchange> {

	DefinitionRepository<ExchangeDefinition> repository;
	
	public DynamicExchangeFactory(ExpressionEvaluator evaluator, DefinitionRepository<ExchangeDefinition> repository) {
		super(evaluator);
		
		this.repository = repository;
	}

	@Override
	public Exchange build(String context) {
		
		ExchangeDefinition definition = this.repository.get(context);
		
		String name = evaluate(definition.getNameExpression(), String.class);
		
		String type = evaluate(definition.getExchangeTypeExpression(), String.class);
		
		String vhost = evaluate(definition.getVirtualHostExpression(), String.class);
		
		boolean isAutoDelete = 
				evaluate(definition.getIsAutoDeleteExpression(), Boolean.class);
		
		boolean isDurable = 
				evaluate(definition.getIsDurableExpression(), Boolean.class);
		
		boolean shouldDeclare = 
				evaluate(definition.getShouldDeclareExpression(), Boolean.class);
		
		Map<String, Object> arguments = 
				evaluateArgumentExpressions(definition.getArgumentExpressions());
		
		return Exchange.builder()
				.name(name)
				.type(type)
				.vhost(vhost)
				.isAutoDelete(isAutoDelete)
				.isDurable(isDurable)
				.declare(shouldDeclare)
				.arguments(arguments)
				.build();
	}

}
