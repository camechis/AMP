package amp.topology.core.repo.rmq;

import java.util.ArrayList;

import rabbitmq.mgmt.model.ListenerContext;
import rabbitmq.mgmt.model.Overview;
import amp.bus.rabbit.topology.Broker;
import amp.bus.rabbit.topology.Exchange;
import amp.bus.rabbit.topology.Queue;
import amp.topology.core.model.Cluster;

public class ModelUtils {

	public static Exchange transform(rabbitmq.mgmt.model.Exchange exchange){
		
		return Exchange.builder()
				.name(exchange.getName())
				.type(exchange.getType())
				.vhost(exchange.getVhost())
				.isDurable(exchange.isDurable())
				.isAutoDelete(exchange.isAutoDelete())
				.declare(false)
				.build();
	}
	
	public static Queue transform(rabbitmq.mgmt.model.Queue queue){
		
		return Queue.builder()
				.name(queue.getName())
				.isAutoDelete(queue.isAutoDelete())
				.isDurable(queue.isDurable())
				.declare(false)
				.build();
	}
	
	public static Cluster transform(String clusterId, String description, Overview overview){
		
		ArrayList<Broker> brokers = new ArrayList<Broker>();
		
		for (ListenerContext listener : overview.getListeners()){
			
			brokers.add( transform(clusterId, listener) );
		}
		
		return new Cluster(clusterId, description, brokers);
	}

	public static Broker transform(String clusterId, ListenerContext listener){
		
		String hostname = (listener.getNode() == null 
				|| listener.getNode().isEmpty())? 
					listener.getIPAddress() : listener.getNode();
		
		boolean useSsl = listener.getProtocol().contains("ssl");
					
		return Broker.builder()
				.cluster(clusterId)
				.host(hostname)
				.port(listener.getPort())
				.useSsl(useSsl)
				.build();
	}
}
