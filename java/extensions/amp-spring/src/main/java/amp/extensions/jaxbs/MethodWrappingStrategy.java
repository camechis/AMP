package amp.extensions.jaxbs;

public interface MethodWrappingStrategy {

	/**
	 * Is this the appropriate strategy to use for this method.
	 * @param context Represents the results of an analysis of the method
	 * and the methods requirements.
	 * @return True if this strategy can handle it this method.
	 */
	boolean isAppropriateStrategy(WrappedMethodContext context);
	
	/**
	 * Wrap the method with whatever functionality is needed.
	 * @param context Represents the object, method, and additional info
	 * necessary to wrap the method with a JAX-BS handler.
	 * @throws Exception For a whole host of reasons.
	 */
	void wrapMethod(WrappedMethodContext context) throws Exception;
}
