package amp.topology.core.factory.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import amp.topology.core.factory.ChainedTopologyFactory;
import amp.topology.core.factory.DelegatingModelFactory;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.factory.TopologyFactory;

public class DefaultDelegatingModelFactory implements DelegatingModelFactory {

	protected ConcurrentHashMap<String, TopologyFactory<?>> factories = 
			new ConcurrentHashMap<String, TopologyFactory<?>>();
	
	public DefaultDelegatingModelFactory(){}
	
	public DefaultDelegatingModelFactory(Collection<TopologyFactory<?>> factories){
		
		this.setFactoryDelegates(factories);
	}
	
	@Override
	public void addFactoryDelegate(TopologyFactory<?> factory) {
		
		if (ChainedTopologyFactory.class.isInstance(factory)){
			
			((ChainedTopologyFactory<?>)factory).setDelegatingModelFactory(this);
		}
		
		this.factories.put(factory.getFactoryName(), factory);
	}
	
	public void setFactoryDelegates(Collection<TopologyFactory<?>> factories){
		
		for (TopologyFactory<?> factory : factories){
			
			addFactoryDelegate(factory);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T make(FactoryReference<T> reference) {
		
		TopologyFactory<?> factory = this.factories.get(reference.getFactoryName());
		
		return (T) factory.build(reference.getContext());
	}
}
