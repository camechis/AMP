package amp.topology.core.factory;

public class FactoryReference<T> {

	protected String factoryName;
	protected String context;
	
	public FactoryReference(){}
	
	public FactoryReference(String factoryName, String context) {
		
		this.factoryName = factoryName;
		this.context = context;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public String getContext() {
		return context;
	}

	@Override
	public String toString() {
		return "FactoryReference [factoryName=" + factoryName + ", context="
				+ context + "]";
	}
}