package amp.rabbit.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Objects;

/**
 * RoutingInfo is the information needed by the Topology Client to creating connections
 * to brokers, and bind to exchanges and queues.
 * 
 * RoutingInfo includes both the information to produce, as well as, consume messages.  We
 * have made the decision to separate these concepts because in our model, we believe that
 * you may produce on one route and consume from another for security reasons.
 * 
 * We also force client to accept a collection of producing and consuming routes.  The intent
 * is that you may need to push/pull messages to/from multiple clusters based on topology.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class RoutingInfo {

    protected ArrayList<ProducingRoute> producingRoutes = 
    		new ArrayList<ProducingRoute>();
    protected ArrayList<ConsumingRoute> consumingRoutes = 
    		new ArrayList<ConsumingRoute>();
    
    public RoutingInfo(){}
    public RoutingInfo(Collection<ProducingRoute> producingRoutes, 
    				   Collection<ConsumingRoute> consumingRoutes){
    	
    		this.producingRoutes.addAll(producingRoutes);
    		this.consumingRoutes.addAll(consumingRoutes);
    }
    
	public List<ProducingRoute> getProducingRoutes() {
		return producingRoutes;
	}
	
	public List<ConsumingRoute> getConsumingRoutes() {
		return consumingRoutes;
	}
	
	public List<BaseRoute> getAllRoutes() {
		ArrayList<BaseRoute> routes = new ArrayList<BaseRoute>();
		routes.addAll(producingRoutes);
		routes.addAll(consumingRoutes);
		return routes;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (other ==null) 
			return false;
		
		if (other.getClass() != this.getClass()) 
			return false;
		
		RoutingInfo otherRoutingInfo = (RoutingInfo) other;
		
		List<ConsumingRoute> croutes1 = consumingRoutes;
		List<ConsumingRoute> croutes2 = otherRoutingInfo.getConsumingRoutes();
		List<ProducingRoute> proutes1 = producingRoutes;
		List<ProducingRoute> proutes2 = otherRoutingInfo.getProducingRoutes();
		
		if (croutes1.size() != croutes2.size())
			return false;

		if (proutes1.size() != proutes2.size())
			return false;
		
		Iterator<ConsumingRoute> croute1it = croutes1.iterator();
		Iterator<ConsumingRoute> croute2it = croutes2.iterator();
		
		while (croute1it.hasNext()) {
			ConsumingRoute cr1= croute1it.next();
			ConsumingRoute cr2= croute2it.next();
			if (!Objects.equal(cr1,cr2)) 
				return false;
		}
		
		Iterator<ProducingRoute> proute1it = proutes1.iterator();
		Iterator<ProducingRoute> proute2it = proutes2.iterator();
		
		while (proute1it.hasNext()) {
			ProducingRoute pr1= proute1it.next();
			ProducingRoute pr2= proute2it.next();
			if (!Objects.equal(pr1,pr2)) 
				return false;
		}
		return true;	
	}
}