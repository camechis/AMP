package amp.topology.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.RouteInfo;
import amp.bus.rabbit.topology.RoutingInfo;
import amp.bus.rabbit.topology.ITopologyService;
import cmf.bus.EnvelopeHeaderConstants;



public class GlobalTopologyService implements ITopologyService {

	ITopologyRepository topologyRepository;
	
	public GlobalTopologyService(ITopologyRepository topologyRepository){
	
		this.topologyRepository = topologyRepository;
	}

	@Override
	public RoutingInfo getRoutingInfo(Map<String, String> routingHints) {
		
		String topic = routingHints.get(EnvelopeHeaderConstants.MESSAGE_TOPIC);
		String client = routingHints.get(EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY);
		
		ArrayList<RouteInfo> routeInfos = new ArrayList<RouteInfo>();
		
		Collection<ExtendedRouteInfo> extendedRouteInfos = this.topologyRepository.find(topic, client);
		
		for (ExtendedRouteInfo extendedRouteInfo : extendedRouteInfos){
			
			Exchange producerExchange = 
				this.topologyRepository.getExchange(
					extendedRouteInfo.getProducerExchangeId())
						.toExchange();
			
			ExtendedExchange consumerExtendedExchange =
				this.topologyRepository.getExchange(
					extendedRouteInfo.getConsumerExchangeId());
			
			ensureValidQueue(consumerExtendedExchange, topic, client);
			
			Exchange consumerExchange = consumerExtendedExchange.toExchange();
			
			routeInfos.add(new RouteInfo(producerExchange, consumerExchange));
		}
		
		return new RoutingInfo(routeInfos);
	}

	@Override
	public void dispose() {}

	private static long QUEUE_COLLISION_INSURANCE_NUMBER = 0l;
	
	protected synchronized static long getQueueCollisionInsurance(){
		
		// In the rare event that we have somehow created 9 quintillion
		// queues, we will rollover back to zero.  This was created
		// to make John Ruiz feel better at night.
		if (QUEUE_COLLISION_INSURANCE_NUMBER == Long.MAX_VALUE){
			
			QUEUE_COLLISION_INSURANCE_NUMBER = 0;
		}
		
		return ++QUEUE_COLLISION_INSURANCE_NUMBER;
	}
	
	protected void ensureValidQueue(ExtendedExchange exchange, String topic, String client){
		
		if (exchange.getQueueName() == null || exchange.getQueueName().equals("")){
			
			String queueName = String.format("%s#%03d#%s", client, getQueueCollisionInsurance(), topic);
			
			exchange.setQueueName(queueName);
		}
	}
	
}
