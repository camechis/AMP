package amp.topology.rmq.repo;

import java.util.Collection;

import amp.bus.rabbit.topology.Broker;

public interface BrokerRepository {

	Collection<Broker> all(String cluster);
	
	Broker get(String cluster, String nodeName);
	
}