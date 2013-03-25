package amp.extensions.commons.builder;

import amp.bus.ITransportProvider;
import amp.bus.rabbit.IRabbitChannelFactory;
import amp.bus.rabbit.RabbitTransportProvider;
import amp.bus.rabbit.topology.ITopologyService;

/**
 * A little fluent interface for configuring the
 * Transport Provider used with CMF+AMP.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class TransportBuilder extends FluentExtension {

	ITopologyService topologyService = null;
	IRabbitChannelFactory channelFactory = null;
	ITransportProvider transportProvider = null;
	
	/**
	 * Initialize the builder with a reference to the parent fluent interface.
	 * @param parent Parent builder.
	 */
	public TransportBuilder(BusBuilder parent) {
	
		super(parent);
	}

	/**
	 * Select and configure the topology to use.
	 * @return Topology Builder interface.
	 */
	public TopologyBuilder routeWith(){
		
		return new TopologyBuilder(this.parent, this);
	}
	
	/**
	 * Select the Channel Factory to use.
	 * @return Channel Factory Builder interface.
	 */
	public ChannelFactoryBuilder connectWith(){
		
		return new ChannelFactoryBuilder(this.parent, this);
	}

	/**
	 * Set the Topology Service.
	 * @param topologyService Topology Service to use.
	 */
	public void setTopologyService(ITopologyService topologyService){
		
		this.topologyService = topologyService;
	}
	
	/**
	 * Set the Channel Factory.
	 * @param channelFactory Channel Factory to use.
	 */
	public void setChannelFactory(IRabbitChannelFactory channelFactory){
		
		this.channelFactory = channelFactory;
	}
	
	/**
	 * Se the Transport Provider (do this if you enjoy pain!).
	 * @param transportProvider Transport Provider to use.
	 */
	public void setTransportProvider(ITransportProvider transportProvider){
		
		this.transportProvider = transportProvider;
	}

	void buildTransportProvider(){
		
		assert this.topologyService != null && this.channelFactory != null;
		
		this.setTransportProvider(
			new RabbitTransportProvider(this.topologyService, this.channelFactory));
	}
	
	/**
	 * And go back to the primary fluent interface.
	 * @return original interface.
	 */
	@Override
	public BusBuilder and() {
		
		return super.and();
	}
}