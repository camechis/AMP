package amp.bus.rabbit.topology;


import java.util.ArrayList;
import java.util.Collection;

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

    protected ArrayList<ProducingRoute> producingRoutes = new ArrayList<ProducingRoute>();
    protected ArrayList<ConsumingRoute> consumingRoutes = new ArrayList<ConsumingRoute>();
    
    public RoutingInfo(){}
    
    public RoutingInfo(Collection<ProducingRoute> producingRoutes, Collection<ConsumingRoute> consumingRoutes){
    	
    		this.producingRoutes.addAll(producingRoutes);
    		this.consumingRoutes.addAll(consumingRoutes);
    }
    
	public ArrayList<ProducingRoute> getProducingRoutes() {
		return producingRoutes;
	}
	
	public ArrayList<ConsumingRoute> getConsumingRoutes() {
		return consumingRoutes;
	}
}