package amp.bus.rabbit.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ConsumingRoute extends BaseRoute {

	Queue queue;

	public ConsumingRoute(){}
	
	public ConsumingRoute(ArrayList<Broker> brokers, NExchange exchange, Queue queue, String routingKey) {
		super(brokers, exchange, routingKey);
		
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
				+ ", exchange=" + exchange + ", routingKey=" + routingKey + "]";
	}

	public static ConsumingRouteBuilder build(){
		
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
		
		public ConsumingRouteBuilder exchange(NExchange exchange){
			
			consumingRoute.setExchange(exchange);
			
			return this;
		}
		
		public ConsumingRouteBuilder queue(Queue queue){
			
			consumingRoute.setQueue(queue);
			
			return this;
		}
		
		public ConsumingRouteBuilder routingkey(String routingkey){
			
			consumingRoute.setRoutingKey(routingkey);
			
			return this;
		}
		
		public ConsumingRoute done(){
			
			return this.consumingRoute;
		}
	}
}