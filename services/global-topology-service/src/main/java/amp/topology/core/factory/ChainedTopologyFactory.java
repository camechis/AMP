package amp.topology.core.factory;

public interface ChainedTopologyFactory<T> extends TopologyFactory<T> {

	void setDelegatingModelFactory(DelegatingModelFactory factory);
	
}
