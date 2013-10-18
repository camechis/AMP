package amp.rabbit.topology;

import java.util.HashMap;

import org.junit.*;

import static org.junit.Assert.*;

public class AmqpBaseModelTests {

	AmqpBaseModel referenceModel = new Model();
	AmqpBaseModel equivelentModel = new Model();
	AmqpBaseModel notEquivelentModel = new Model();
	
	@Before
	public void setup(){
		
		referenceModel.arguments = new HashMap<String,Object>();
		referenceModel.arguments.put("1", "one");

		equivelentModel.arguments = new HashMap<String,Object>();
		equivelentModel.arguments.put("1", "one");

		notEquivelentModel.arguments = new HashMap<String,Object>();
		notEquivelentModel.arguments.put("1", "uno");

	}

	@Test
	public void Equality_should_consider_argument_contents(){
		assertTrue(referenceModel.equals(equivelentModel));
	}
	
	@Test
	public void Equality_should_consider_argument_contents_not_equals(){
		assertFalse(referenceModel.equals(notEquivelentModel));
	}
	
	@Test
	public void Hashcodes_should_not_differ_if_noting_differs(){
		assertEquals(referenceModel.hashCode() , equivelentModel.hashCode());
	}

	@Test
	public void Hashcodes_should_differ_if_argument_contents_differ(){
		assertNotEquals(referenceModel.hashCode() , notEquivelentModel.hashCode());
	}

	private static class Model extends AmqpBaseModel { }
}
