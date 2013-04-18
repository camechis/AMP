package amp.bus.rabbit.topology;

import java.util.ArrayList;
import java.util.Collection;

public class BaseRoute {

	ArrayList<Broker> brokers = new ArrayList<Broker>();
	
	NExchange exchange;
	
	String routingKey;
	
	public BaseRoute(){}
	
	public BaseRoute(ArrayList<Broker> brokers, NExchange exchange,
			String routingKey) {
		
		this.brokers = brokers;
		this.exchange = exchange;
		this.routingKey = routingKey;
	}
	
	public void setBrokers(Collection<Broker> brokers){
		
		this.brokers.addAll(brokers);
	}
	
	public Collection<Broker> getBrokers(){
		
		return this.brokers;
	}
	
	public void setExchange(NExchange exchange){
		
		this.exchange = exchange;
	}
	
	public NExchange getExchange(){
		
		return this.exchange;
	}
	
	public void setRoutingKey(String routingKey){
		
		this.routingKey = routingKey;
	}
	
	public String getRoutingKey(){
		
		return this.routingKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brokers == null) ? 0 : brokers.hashCode());
		result = prime * result
				+ ((exchange == null) ? 0 : exchange.hashCode());
		result = prime * result
				+ ((routingKey == null) ? 0 : routingKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseRoute other = (BaseRoute) obj;
		if (brokers == null) {
			if (other.brokers != null)
				return false;
		} else if (!brokers.equals(other.brokers))
			return false;
		if (exchange == null) {
			if (other.exchange != null)
				return false;
		} else if (!exchange.equals(other.exchange))
			return false;
		if (routingKey == null) {
			if (other.routingKey != null)
				return false;
		} else if (!routingKey.equals(other.routingKey))
			return false;
		return true;
	}
}
