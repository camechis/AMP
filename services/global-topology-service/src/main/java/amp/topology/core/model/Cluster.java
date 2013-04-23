package amp.topology.core.model;

import java.util.ArrayList;
import java.util.Collection;

import amp.bus.rabbit.topology.Broker;

public class Cluster extends TopologyModel {
	
	protected ArrayList<Broker> brokers = new ArrayList<Broker>();
	
	public Cluster(){ super(); }
	
	public Cluster(String id, String description, Collection<Broker> brokers) {
		
		super(id, description);
		
		this.brokers.addAll(brokers);
	}

	public Collection<Broker> getBrokers() {
		return brokers;
	}
}
