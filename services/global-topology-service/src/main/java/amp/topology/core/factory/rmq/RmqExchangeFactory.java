package amp.topology.core.factory.rmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.Exchange;
import amp.eventing.ISerializer;
import amp.topology.core.factory.impl.ContextMarshallingFactory;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.rmq.ModelUtils;
import amp.topology.rmq.repo.ExchangeRepository;

public class RmqExchangeFactory extends ContextMarshallingFactory<Exchange, RmqCompositeKey> {

	private static final Logger logger = LoggerFactory.getLogger(RmqExchangeFactory.class);
	
	ExchangeRepository exchangeRepository;
	
	public RmqExchangeFactory(
			ISerializer serializer, ExchangeRepository exchangeRepository) {
		
		super(serializer);
		
		this.exchangeRepository = exchangeRepository;
	}

	@Override
	public Class<RmqCompositeKey> getReferenceClass() {
		
		return RmqCompositeKey.class;
	}
	
	@Override
	public Exchange build(RmqCompositeKey ref) {
		
		Exchange exchange = null;
		
		try {
			
			exchange = ModelUtils.transform(
				this.exchangeRepository.get(ref.getCluster(), ref.getCluster(), ref.getName()));
		
		} catch (ClusterDoesntExistException cdee){
			
			logger.error("Cluster does not exist: {}", cdee);
		}
		
		return exchange;
	}
}