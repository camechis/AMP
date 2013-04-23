package amp.topology.core.factory;


/**
 * Given a context identifier, return the specified model object
 * represented by T.
 * 
 * @author Richard Clayton (Berico Technologies)
 *
 * @param <T> Model contruct.
 */
public interface TopologyFactory<T> {

	String getFactoryName();
	
	T build(String context);
}