package amp.topology.core.factory.impl;

import amp.eventing.ISerializer;
import amp.topology.core.factory.TopologyFactory;

public abstract class ContextMarshallingFactory<T, Ref> implements TopologyFactory<T> {

	protected ISerializer serializer;
	
	public ContextMarshallingFactory(ISerializer serializer){
		
		this.serializer = serializer;
	}
	
	public abstract Class<Ref> getReferenceClass();
	
	public abstract T build(Ref reference);
	
	@Override
	public String getFactoryName() {
		
		return this.getClass().getCanonicalName();
	}
	
	@Override
	public T build(String context) {
		
		Ref reference = serializer.stringDeserialize(context, getReferenceClass());
		
		return build(reference);
	}
}
