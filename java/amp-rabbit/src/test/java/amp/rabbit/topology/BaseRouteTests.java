package amp.rabbit.topology;

import org.junit.*;

import static org.junit.Assert.*;

public class BaseRouteTests {

	protected BaseRoute referenceModel = new Route();
	protected BaseRoute equivelentModel = new Route();
	protected BaseRoute brokerNotEquivelentModel = new Route();
	protected BaseRoute exchangeNotEquivelentModel = new Route();
	protected BaseRoute routeNotEquivelentModel = new Route();
	
	@Before
	public void setup(){
		
		referenceModel.brokers.add(new Broker(null,"reference", 0, "basic"));
		referenceModel.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		referenceModel.routingKeys.add("route");

		equivelentModel.brokers.add(new Broker(null,"reference", 0, "basic"));
		equivelentModel.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		equivelentModel.routingKeys.add("route");

		brokerNotEquivelentModel.brokers.add(new Broker(null,"reference", 0, "ssl"));
		brokerNotEquivelentModel.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		brokerNotEquivelentModel.routingKeys.add("route");

		exchangeNotEquivelentModel.brokers.add(new Broker(null,"reference", 0, "basic"));
		exchangeNotEquivelentModel.exchange = new Exchange("other", "direct", true, true, true, null, null);
		exchangeNotEquivelentModel.routingKeys.add("route");

		routeNotEquivelentModel.routingKeys.add("other-route");
		routeNotEquivelentModel.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		routeNotEquivelentModel.brokers.add(new Broker(null,"reference", 0, "basic"));
	}

	@Test
	public void Equals_should_return_true_for_equivelent_routes(){
		assertTrue(referenceModel.equals(equivelentModel));
	}
	
	@Test
	public void Equals_should_return_false_if_broker_values_differ(){
		assertFalse(referenceModel.equals(brokerNotEquivelentModel));
	}
	
	@Test
	public void Equals_should_return_false_if_exchange_values_differ(){
		assertFalse(referenceModel.equals(exchangeNotEquivelentModel));
	}
	
	@Test
	public void Equals_should_return_false_if_route_values_differ(){
		assertFalse(referenceModel.equals(routeNotEquivelentModel));
	}
	
	@Test
	public void Hashcodes_should_not_differ_for_equivelent_routes(){
		assertEquals(referenceModel.hashCode() , equivelentModel.hashCode());
	}

	@Test
	public void Hashcodes_should_differ_if_broker_entries_differ(){
		assertNotEquals(referenceModel.hashCode() , brokerNotEquivelentModel.hashCode());
	}
	
	@Test
	public void Hashcodes_should_differ_if_exchanges_differ(){
		assertNotEquals(referenceModel.hashCode() , exchangeNotEquivelentModel.hashCode());
	}
	
	@Test
	public void Hashcodes_should_differ_if_routingKey_entries_differ(){
		assertNotEquals(referenceModel.hashCode() , routeNotEquivelentModel.hashCode());
	}
	
	private static class Route extends BaseRoute {
	}
}
