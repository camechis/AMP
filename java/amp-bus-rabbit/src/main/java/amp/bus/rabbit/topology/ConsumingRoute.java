package amp.bus.rabbit.topology;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents the information needed to consume messages by the transport provider.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ConsumingRoute extends BaseRoute {

	Queue queue;

	public ConsumingRoute(){}
	
	public ConsumingRoute(Collection<Broker> brokers, Exchange exchange, Queue queue, Collection<String> routingKeys) {
		super(brokers, exchange, routingKeys);
		
		this.queue = queue;
	}
	
	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	@Override
	public String toString() {
		return "ConsumingRoute [queue=" + queue + ", brokers=" + brokers
				+ ", exchange=" + exchange + ", routingKey=" + routingKeys + "]";
	}

	public static ConsumingRouteBuilder builder(){
		
		return new ConsumingRouteBuilder();
	}
	
	public static class ConsumingRouteBuilder {
		
		ConsumingRoute consumingRoute = new ConsumingRoute();
		
		public ConsumingRouteBuilder brokers(Broker... brokers){
			
			consumingRoute.setBrokers(Arrays.asList(brokers));
			
			return this;
		}
		
		public ConsumingRouteBuilder brokers(Collection<Broker> brokers){
			
			consumingRoute.setBrokers(brokers);
			
			return this;
		}
		
		public ConsumingRouteBuilder exchange(Exchange exchange){
			
			consumingRoute.setExchange(exchange);
			
			return this;
		}
		
		public ConsumingRouteBuilder queue(Queue queue){
			
			consumingRoute.setQueue(queue);
			
			return this;
		}
		
		public ConsumingRouteBuilder routingkeys(String... routingkeys){
			
			consumingRoute.setRoutingKeys(Arrays.asList(routingkeys));
			
			return this;
		}
		
		public ConsumingRouteBuilder routingkeys(Collection<String> routingkeys){
			
			consumingRoute.setRoutingKeys(routingkeys);
			
			return this;
		}
		
		public ConsumingRoute build(){
			
			return this.consumingRoute;
		}
	}
}