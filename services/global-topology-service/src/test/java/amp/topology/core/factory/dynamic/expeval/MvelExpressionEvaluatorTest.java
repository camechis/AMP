package amp.topology.core.factory.dynamic.expeval;

import static org.junit.Assert.*;

import org.junit.Test;

public class MvelExpressionEvaluatorTest {

	MvelExpressionEvaluator getEvaluator(){
		
		return new MvelExpressionEvaluator();
	}
	
	@Test
	public void strings_are_fail_evaluation_and_are_returned_intact() {
		
		String expression = "amp.example.of.non.Expression";
		
		MvelExpressionEvaluator evaluator = getEvaluator();
		
		String actual = evaluator.evaluate(expression, String.class);
		
		assertEquals(expression, actual);
	}
	
	
	@Test
	public void complex_expressions_evaluate_and_are_returned_as_requested_type(){
		
		String expression = "1 + 1";
		
		int expected = 2;
		
		MvelExpressionEvaluator evaluator = getEvaluator();
		
		int actual = evaluator.evaluate(expression, Integer.class);
		
		assertEquals(expected, actual);
	}

	@Test
	public void string_template_evaluated_correctly(){
		
		String expression = "amp.complex.type.event@{1 + 1}.hello.World";
		
		String expected = "amp.complex.type.event2.hello.World";
		
		MvelExpressionEvaluator evaluator = getEvaluator();
		
		String actual = evaluator.evaluate(expression, String.class);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void true_valued_booleans_are_parsed_and_returned_as_boolean_value(){
		
		String expression = "true";
		
		boolean expected = true;
		
		MvelExpressionEvaluator evaluator = getEvaluator();
		
		boolean actual = evaluator.evaluate(expression, Boolean.class);
		
		assertEquals(expected, actual);
		
	}
	
	@Test
	public void false_valued_booleans_are_parsed_and_returned_as_boolean_value(){
		
		String expression = "false";
		
		boolean expected = false;
		
		MvelExpressionEvaluator evaluator = getEvaluator();
		
		boolean actual = evaluator.evaluate(expression, Boolean.class);
		
		assertEquals(expected, actual);
		
	}
}
