package amp.topology.core.factory.dynamic;

import java.util.ArrayList;
import java.util.Collection;

import amp.topology.core.model.RoutingContext;

public class Utils {

	public static Collection<String> asRoutingKeys(
			Collection<RoutingContext> routingContexts, 
			ExpressionEvaluator evaluator){
		
		ArrayList<String> routingKeys = new ArrayList<String>();
		
		for (RoutingContext context : routingContexts){
		
			String evaluatedExpression = evaluator.evaluate(context.getValue(), String.class);
			
			routingKeys.add(evaluatedExpression);
		}
		
		return routingKeys;
	}
	
}
