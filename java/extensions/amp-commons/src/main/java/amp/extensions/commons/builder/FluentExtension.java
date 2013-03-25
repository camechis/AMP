package amp.extensions.commons.builder;

/**
 * Just defines a quick and easy way to get back to the
 * parent fluent interface.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public abstract class FluentExtension {

	BusBuilder parent;
	
	/**
	 * Instantiate with reference to the parent.
	 * @param parent Parent Fluent.
	 */
	public FluentExtension(BusBuilder parent){
		
		this.parent = parent;
	}
	
	/**
	 * And go back to the parent.
	 * @return Parent fluent.
	 */
	public BusBuilder and(){
		
		return this.parent;
	}
	
}
