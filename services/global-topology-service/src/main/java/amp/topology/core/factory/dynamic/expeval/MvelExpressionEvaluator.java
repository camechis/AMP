package amp.topology.core.factory.dynamic.expeval;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.factory.dynamic.ExpressionEvaluator;

public class MvelExpressionEvaluator implements ExpressionEvaluator {

	private static final Logger logger = 
			LoggerFactory.getLogger(MvelExpressionEvaluator.class);
	
	public static String STRING_EXPRESSION_FLAG = "@{";
	
	HashMap<String, Object> context = new HashMap<String, Object>();
	
	public MvelExpressionEvaluator(){}
	
	public MvelExpressionEvaluator(Map<String, Object> context){
		
		setContext(context);
	}
	
	public void setContext(Map<String, Object> context){
		
		this.context.putAll(context);
	}
	
	public void registerVariable(String variableName, Object variable){
		
		this.context.put(variableName, variable);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T evaluate(String expression, Class<T> returnType) {
		
		logger.debug("Evaluating expression: {}, returning type: {}", expression, returnType);
		
		if (expression.contains(STRING_EXPRESSION_FLAG) 
			&& String.class.isAssignableFrom(returnType)){
			
			logger.debug("Was a string template.");
			
			return (T) TemplateRuntime.eval(expression, this.context);
		}
		
		T result = null;
		
		try {
		
			logger.debug("Attempting to evaluate expression.");
			
			result = MVEL.eval(expression, context, returnType);
			
		} catch (Exception e){
			
			logger.debug("Expression evaluation failed: {}", e.getMessage());
			
			if (String.class.isAssignableFrom(returnType)){
				
				logger.debug("Expression is a string, so returning intact.");
				
				return (T) expression;
			}
		}
		
		logger.debug("Returning result: {}", result);
		
		return result;
	}

}
