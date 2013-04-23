package amp.topology.core.factory;

/**
 * Construct the model object of desired type from the supplied reference,
 * by delegating the work to the correct factory.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public interface DelegatingModelFactory {
	
	void addFactoryDelegate(TopologyFactory<?> factory);
	
	<T> T make(FactoryReference<T> reference);

}