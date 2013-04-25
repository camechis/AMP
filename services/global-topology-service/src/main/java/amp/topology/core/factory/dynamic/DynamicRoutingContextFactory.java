package amp.topology.core.factory.dynamic;

import amp.topology.core.factory.dynamic.repo.DefinitionRepository;
import amp.topology.core.model.RoutingContext;
import amp.topology.core.model.definitions.RoutingContextDefinition;

public class DynamicRoutingContextFactory extends EvaluatingChainedTopologyFactory<RoutingContext> {

	DefinitionRepository<RoutingContextDefinition> repository;

	public DynamicRoutingContextFactory(ExpressionEvaluator evaluator,
			DefinitionRepository<RoutingContextDefinition> repository) {
		super(evaluator);
		
		this.repository = repository;
	}
	
	@Override
	public RoutingContext build(String context) {
		
		RoutingContextDefinition definition = this.repository.get(context);
		
		String value = this.evaluate(definition.getValueExpression(), String.class);
		
		return new RoutingContext(definition.getId(), definition.getDescription(), value);
	}

}
