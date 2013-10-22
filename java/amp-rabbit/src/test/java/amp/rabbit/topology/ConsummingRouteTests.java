package amp.rabbit.topology;

import static org.junit.Assert.*;

import org.junit.*;

public class ConsummingRouteTests extends BaseRouteTests {
	
	protected ConsumingRoute queueNotEquivelentModel = new ConsumingRoute();

	@Override 
	public void setup() {
		referenceModel = new ConsumingRoute();
		equivelentModel = new ConsumingRoute();
		brokerNotEquivelentModel = new ConsumingRoute();
		exchangeNotEquivelentModel = new ConsumingRoute();
		routeNotEquivelentModel = new ConsumingRoute();

		super.setup();
		
		((ConsumingRoute)referenceModel).queue = new Queue("reference", true, true, true, true, null, null);
		((ConsumingRoute)equivelentModel).queue = new Queue("reference", true, true, true, true, null, null);
		((ConsumingRoute)brokerNotEquivelentModel).queue = new Queue("reference", true, true, true, true, null, null);
		((ConsumingRoute)exchangeNotEquivelentModel).queue = new Queue("reference", true, true, true, true, null, null);
		((ConsumingRoute)routeNotEquivelentModel).queue = new Queue("reference", true, true, true, true, null, null);

		queueNotEquivelentModel.brokers.add(new Broker("reference", 0, "basic"));
		queueNotEquivelentModel.exchange = new Exchange("reference", "direct", true, true, true, null, null);
		queueNotEquivelentModel.routingKeys.add("route");
		queueNotEquivelentModel.queue = new Queue("other-queue", true, true, true, true, null, null);
		
	}
	
	@Test
	public void Equals_should_return_false_if_queue_values_differ(){
		assertFalse(referenceModel.equals(queueNotEquivelentModel));
	}
	
	@Test
	public void Hashcodes_should_differ_if_queue_entries_differ(){
		assertNotEquals(referenceModel.hashCode() , queueNotEquivelentModel.hashCode());
	}
}
