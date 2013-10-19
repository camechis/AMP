package amp.rabbit.topology;

import java.util.HashMap;

import org.junit.*;

import static org.junit.Assert.*;

public class ExchangeTests extends AmqpBaseModelTests{

	protected Exchange typeNotEquivelentModel = new Exchange();
	
	@Override
	public void setup(){
		
	    referenceModel = new Exchange();
		equivelentModel = new Exchange();
		notEquivelentModel = new Exchange();
		
		super.setup();
		
		((Exchange)referenceModel).setExchangeType("direct");
		((Exchange)equivelentModel).setExchangeType("direct");
		((Exchange)notEquivelentModel).setExchangeType("direct");

		typeNotEquivelentModel.arguments = new HashMap<String,Object>();
		typeNotEquivelentModel.arguments.put("1", "one");
		typeNotEquivelentModel.setExchangeType("topic");

	}
	
	@Test
	public void Equality_should_consider_exchange_type(){
		assertFalse(referenceModel.equals(typeNotEquivelentModel));
	}

	@Test
	public void Hashcodes_should_differ_if_exchange_type_differ(){
		assertNotEquals(referenceModel.hashCode() , typeNotEquivelentModel.hashCode());
	}
}
