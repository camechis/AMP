package amp.bus.rabbit;

import amp.bus.rabbit.BaseChannelFactory.ConnectionContext;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;

public interface ConnectionReuseStrategy {
	
	public boolean shouldReuse(ProducingRoute testRoute, ConnectionContext context);
	
	public boolean shouldReuse(ConsumingRoute testRoute, ConnectionContext context);
	
	/**
	 * Don't reuse any connection.
	 */
	public static ConnectionReuseStrategy NO_REUSE = new ConnectionReuseStrategy(){

		@Override
		public boolean shouldReuse(
			ProducingRoute testRoute, ConnectionContext context) {
			
			return false;
		}

		@Override
		public boolean shouldReuse(
			ConsumingRoute testRoute, ConnectionContext context) {
			
			return false;
		}
	};
	
	/**
	 * Reuse connections where the route is on the same broker.
	 */
	public static ConnectionReuseStrategy SAME_BROKER = new ConnectionReuseStrategy(){

		@Override
		public boolean shouldReuse(
				ProducingRoute testRoute, ConnectionContext context) {
			
			for (Broker b : testRoute.getBrokers()){
				
				if (context.getActiveBroker().equals(b)){
					
					return true;
				}
			}
			
			return false;
		}

		@Override
		public boolean shouldReuse(
				ConsumingRoute testRoute, ConnectionContext context) {
			
			for (Broker b : testRoute.getBrokers()){
				
				if (context.getActiveBroker().equals(b)){
					
					return true;
				}
			}
			
			return false;
		}
	};
}
