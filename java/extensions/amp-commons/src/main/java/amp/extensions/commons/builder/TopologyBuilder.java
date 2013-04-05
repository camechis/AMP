package amp.extensions.commons.builder;

/**
 * A little fluent interface for selecting the type of
 * Topology Service you will want to use with CMF+AMP.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class TopologyBuilder extends FluentExtension {

	TransportBuilder transportBuilder;
	
	/**
	 * Initialize the builder with a reference to the parent fluent interface.
	 * @param parent Parent builder.
	 * @param transportBuilder Transport Builder instance.
	 */
	public TopologyBuilder(BusBuilder parent, TransportBuilder transportBuilder) {
		
		super(parent);
		
		this.transportBuilder = transportBuilder;
	}

	/**
	 * Use a globally managed topology.
	 * @return Global Topology Fluent.
	 */
	public GlobalTopologyBuilder globalTopology(){
		
		return new GlobalTopologyBuilder(this.parent, this.transportBuilder);
	}
	
	/**
	 * Use a simple topology.
	 * @return Simple Topology Fluent.
	 */
	public SimpleTopologyBuilder simpleTopology(){
		
		return new SimpleTopologyBuilder(this.parent, this.transportBuilder);
	}
}