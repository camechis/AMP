package amp.rabbit.topology;

import org.junit.*;

import static org.junit.Assert.*;

public class RoutingInfoTests {

	protected RoutingInfo referenceRoute = new RoutingInfo();
	protected RoutingInfo equivelentRoute = new RoutingInfo();
	protected RoutingInfo producingRoutesNotEquivelentRoute = new RoutingInfo();
	protected RoutingInfo consumingRoutesNotEquivelent = new RoutingInfo();
	
	@Before
	public void setup(){
		
		referenceRoute.consumingRoutes.add(new ConsumingRoute());
		ConsumingRoute consumingRoute = referenceRoute.consumingRoutes.get(0);
		consumingRoute.brokers.add(new Broker(null,"reference", 0, true));
		consumingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		consumingRoute.routingKeys.add("route");
		consumingRoute.queue = new Queue("queue", true, true, true, true, null, null);
		
		referenceRoute.producingRoutes.add(new ProducingRoute());
		ProducingRoute producingRoute = referenceRoute.producingRoutes.get(0);
		producingRoute.brokers.add(new Broker(null,"reference", 0, true));
		producingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		producingRoute.routingKeys.add("route");

		equivelentRoute.consumingRoutes.add(new ConsumingRoute());
		consumingRoute = equivelentRoute.consumingRoutes.get(0);
		consumingRoute.brokers.add(new Broker(null,"reference", 0, true));
		consumingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		consumingRoute.routingKeys.add("route");
		consumingRoute.queue = new Queue("queue", true, true, true, true, null, null);
		
		equivelentRoute.producingRoutes.add(new ProducingRoute());
		producingRoute = equivelentRoute.producingRoutes.get(0);
		producingRoute.brokers.add(new Broker(null,"reference", 0, true));
		producingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		producingRoute.routingKeys.add("route");

		producingRoutesNotEquivelentRoute.consumingRoutes.add(new ConsumingRoute());
		consumingRoute = producingRoutesNotEquivelentRoute.consumingRoutes.get(0);
		consumingRoute.brokers.add(new Broker(null,"reference", 0, true));
		consumingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		consumingRoute.routingKeys.add("route");
		consumingRoute.queue = new Queue("queue", true, true, true, true, null, null);
		
		producingRoutesNotEquivelentRoute.producingRoutes.add(new ProducingRoute());
		producingRoute = producingRoutesNotEquivelentRoute.producingRoutes.get(0);
		producingRoute.brokers.add(new Broker(null,"reference", 0, true));
		producingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		producingRoute.routingKeys.add("other-route");

		consumingRoutesNotEquivelent.consumingRoutes.add(new ConsumingRoute());
		consumingRoute = consumingRoutesNotEquivelent.consumingRoutes.get(0);
		consumingRoute.brokers.add(new Broker(null,"reference", 0, true));
		consumingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		consumingRoute.routingKeys.add("other-route");
		consumingRoute.queue = new Queue("queue", true, true, true, true, null, null);
		
		consumingRoutesNotEquivelent.producingRoutes.add(new ProducingRoute());
		producingRoute = consumingRoutesNotEquivelent.producingRoutes.get(0);
		producingRoute.brokers.add(new Broker(null,"reference", 0, true));
		producingRoute.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		producingRoute.routingKeys.add("route");

	}

	@Test
	public void Equals_should_return_true_for_equivelent_routes(){
		assertTrue(referenceRoute.equals(equivelentRoute));
	}
	
	@Test
	public void Equals_should_return_false_if_consuming_routes_differ(){
		assertFalse(referenceRoute.equals(consumingRoutesNotEquivelent));
	}
	
	@Test
	public void Equals_should_return_false_if_producing_routes_differ(){
		assertFalse(referenceRoute.equals(producingRoutesNotEquivelentRoute));
	}
	
	@Test
	public void Hashcodes_should_not_differ_for_equivelent_routes(){
		assertEquals(referenceRoute.hashCode() , equivelentRoute.hashCode());
	}

	@Test
	public void Hashcodes_should_differ_if_consuming_routes_differ(){
		assertNotEquals(referenceRoute.hashCode() , consumingRoutesNotEquivelent.hashCode());
	}

	@Test
	public void Hashcodes_should_differ_if_producing_routes_differ(){
		assertNotEquals(referenceRoute.hashCode() , producingRoutesNotEquivelentRoute.hashCode());
	}
}
