package amp.topology.core;

import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Binding;
import rabbitmq.mgmt.model.Queue;
import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.RouteInfo;

public class RmqRouteCreator implements RouteCreator {

	private static final Logger logger = LoggerFactory.getLogger(RmqRouteCreator.class);
	
	public static String DELIMITER = "~";
	
	public void setDelimiter(String delimiter){ DELIMITER = delimiter; }
	
	RabbitMgmtService mgmtService;
	
	public RmqRouteCreator(RabbitMgmtService mgmtService) {
		
		this.mgmtService = mgmtService;
	}
	
	@Override
	public void create(Collection<RouteInfo> routeInfos, TopologyRequestContext context) {
		
		logger.info("Processing routes: {}", routeInfos);
		
		Collection<Exchange> exchanges = getUniqueBindings(routeInfos);
		
		logger.info("Declaring exchanges: {}", exchanges);
		
		for (Exchange ex : exchanges){ declareExchange(ex); }
		
		Collection<Exchange> queues = getUniqueQueues(routeInfos, context);
		
		logger.info("Declaring queues: {}", queues);
		
		for (Exchange ex : queues){ declareQueue(ex); }
		
		Collection<Exchange> bindings = getUniqueBindings(routeInfos);
		
		logger.info("Declaring bindings: {}", bindings);
		
		for (Exchange ex : bindings){ declareBinding(ex); }
	}
	
	protected Collection<Exchange> getUniqueExchanges(Collection<RouteInfo> routeInfos){
		
		HashMap<String, Exchange> uniqueEntries = new HashMap<String, Exchange>();
		
		for (RouteInfo ri : routeInfos){
			
			if (ri.getProducerExchange() != null){
			
				String pKey = buildQualifiedName(
					ri.getProducerExchange(), 
					ri.getProducerExchange().getName());
				
				addIfNotExists(uniqueEntries, pKey, ri.getProducerExchange());
			}
			
			if (ri.getConsumerExchange() != null){
				
				String cKey = buildQualifiedName(
						ri.getConsumerExchange(), 
						ri.getConsumerExchange().getName());
				
				addIfNotExists(uniqueEntries, cKey, ri.getConsumerExchange());
			}
		}
		
		return uniqueEntries.values();
	}
	
	protected Collection<Exchange> getUniqueQueues(
			Collection<RouteInfo> routeInfos, TopologyRequestContext context){
		
		HashMap<String, Exchange> uniqueEntries = new HashMap<String, Exchange>();
		
		for (RouteInfo ri : routeInfos){
			
			if (ri.getConsumerExchange() != null){
			
				String queueName = ri.getConsumerExchange().getQueueName();
				
				if (queueName == null || context.getQueueName() != null){
					
					queueName = buildQueueName(ri.getConsumerExchange(), context);
				}
				
				String cKey = buildQualifiedName(
						ri.getConsumerExchange(), queueName);
				
				addIfNotExists(uniqueEntries, cKey, ri.getConsumerExchange());
			}
		}
		
		return uniqueEntries.values();
	}
	
	protected Collection<Exchange> getUniqueBindings(Collection<RouteInfo> routeInfos){
		
		HashMap<String, Exchange> uniqueEntries = new HashMap<String, Exchange>();
		
		for (RouteInfo ri : routeInfos){
			
			if (ri.getConsumerExchange() != null){
				
				String cKey = buildQualifiedName(
						ri.getConsumerExchange(), 
						ri.getConsumerExchange().getRoutingKey());
				
				addIfNotExists(uniqueEntries, cKey, ri.getConsumerExchange());
			}
		}
		
		return uniqueEntries.values();
	}
	
	@SuppressWarnings("unchecked")
	protected void declareExchange(Exchange ex){
		
		rabbitmq.mgmt.model.Exchange rmqEx = 
				new rabbitmq.mgmt.model.Exchange();
		
		rmqEx.setName(ex.getName());
		rmqEx.setType(ex.getExchangeType());
		rmqEx.setAutoDelete(ex.getIsAutoDelete());
		rmqEx.setDurable(ex.getIsDurable());
		rmqEx.setVhost(ex.getVirtualHost());
		rmqEx.setArguments(ex.getArguments());
		
		mgmtService.exchanges().create(rmqEx);
	}
	
	@SuppressWarnings("unchecked")
	protected void declareQueue(Exchange ex){
		
		Queue rmqQ = new Queue();
		rmqQ.setName(ex.getQueueName());
		rmqQ.setVhost(ex.getVirtualHost());
		rmqQ.setDurable(ex.getIsDurable());
		rmqQ.setAutoDelete(ex.getIsAutoDelete());
		rmqQ.setArguments(ex.getArguments());
		
		mgmtService.queues().create(rmqQ);
	}
	
	protected void declareBinding(Exchange ex){
		
		Binding rmqB = new Binding();
		rmqB.setSource(ex.getName());
		rmqB.setVhost(ex.getVirtualHost());
		rmqB.setDestination(ex.getQueueName());
		rmqB.setDestinationType("queue");
		rmqB.setRoutingKey(ex.getRoutingKey());
		
		mgmtService.bindings().create(rmqB);
	}
	
	protected String buildQueueName(Exchange ex, TopologyRequestContext context){
		
		if (context.getQueueName() != null){
			
			return context.getQueueName();
		}
		
		StringBuilder sb = new StringBuilder();
		
		if (context.getQueuePrefix() != null){
			
			sb.append(context.getQueuePrefix()).append(DELIMITER);
		}
		
		sb.append(context.getClient()).append(DELIMITER).append(context.getTopic());
		
		return sb.toString();
	}
	
	
	protected String buildQualifiedName(Exchange ex, String contextKey){
		
		return String.format("amqp://%s:%s%s%s", 
			ex.getHostName(), ex.getPort(), ex.getVirtualHost(), contextKey);
	}
	
	void addIfNotExists(HashMap<String, Exchange> map, String contextKey, Exchange ex){
		
		if (!map.containsKey(contextKey)){
		
			map.put(contextKey, ex);
		}
	}
	
	
	
}
