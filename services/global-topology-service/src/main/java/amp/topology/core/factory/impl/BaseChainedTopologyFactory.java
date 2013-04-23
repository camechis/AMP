package amp.topology.core.factory.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.factory.ChainedTopologyFactory;
import amp.topology.core.factory.DelegatingModelFactory;
import amp.topology.core.factory.FactoryReference;

public abstract class BaseChainedTopologyFactory<T> implements ChainedTopologyFactory<T> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private DelegatingModelFactory delegatingFactory;
	
	@Override
	public String getFactoryName() {
		
		return this.getClass().getCanonicalName();
	}
	
	@Override
	public void setDelegatingModelFactory(DelegatingModelFactory delegatingFactory) {
		
		this.delegatingFactory = delegatingFactory;
	}
	
	protected <TYPE> TYPE constructAggregate(FactoryReference<TYPE> reference){
		
		return this.delegatingFactory.make(reference);
	}
	
	protected <TYPE> Collection<TYPE> constructAggregates(
				Collection<FactoryReference<TYPE>> references){
		
		ArrayList<TYPE> actuals = new ArrayList<TYPE>();
		
		for(FactoryReference<TYPE> reference : references){
			
			actuals.add( this.constructAggregate(reference) );
		}
		
		return actuals;
	}
}