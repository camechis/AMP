package amp.bus.rabbit.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ProducingRoute extends BaseRoute {
	
	public ProducingRoute() { super(); }

	public ProducingRoute(ArrayList<Broker> brokers, NExchange exchange, String routingKey) {
		
		super(brokers, exchange, routingKey);
	}
	
	@Override
	public String toString() {
		return "ProducingRoute [brokers=" + brokers + ", exchange=" + exchange
				+ ", routingKey=" + routingKey + "]";
	}
	
	public static ProducingRouteBuilder build(){
		
		return new ProducingRouteBuilder();
	}
	
	public static class ProducingRouteBuilder {
		
		ProducingRoute producingRoute = new ProducingRoute();
		
		public ProducingRouteBuilder exchange(NExchange exchange){
			
			this.producingRoute.setExchange(exchange);
			
			return this;
		}
		
		public ProducingRouteBuilder brokers(Collection<Broker> brokers){
			
			this.producingRoute.setBrokers(brokers);
			
			return this;
		}
		
		public ProducingRouteBuilder brokers(Broker... brokers){
					
			this.producingRoute.setBrokers(Arrays.asList(brokers));
			
			return this;
		}
		
		public ProducingRouteBuilder routingKey(String routingKey){
			
			this.producingRoute.setRoutingKey(routingKey);
			
			return this;
		}
		
		public ProducingRoute done(){
			
			return this.producingRoute;
		}
	}
}