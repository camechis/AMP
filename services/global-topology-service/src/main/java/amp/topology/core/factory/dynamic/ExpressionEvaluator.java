package amp.topology.core.factory.dynamic;

public interface ExpressionEvaluator {

	<T> T evaluate(String expression, Class<T> returnType);
	
}