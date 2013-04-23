package amp.topology.core.factory.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import amp.topology.core.factory.impl.BaseChainedTopologyFactory;

public abstract class EvaluatingChainedTopologyFactory<T>  extends BaseChainedTopologyFactory<T> {

	private ExpressionEvaluator evaluator;
	
	public EvaluatingChainedTopologyFactory(ExpressionEvaluator evaluator){
		
		this.evaluator = evaluator;
	}
	
	protected <TYPE> TYPE evaluate(String expression, Class<TYPE> returnType){
		
		return this.evaluator.evaluate(expression, returnType);
	}

	protected Map<String, Object> evaluateArgumentExpressions(
			Map<String, String> argumentExpressions){
		
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		
		for (Entry<String, String> argumentExpression : argumentExpressions.entrySet()){
			
			String key = this.evaluate(argumentExpression.getKey(), String.class);
			
			Object value = this.evaluate(argumentExpression.getValue(), Object.class);
			
			arguments.put(key, value);
		}
		
		return arguments;
	}
	
}
