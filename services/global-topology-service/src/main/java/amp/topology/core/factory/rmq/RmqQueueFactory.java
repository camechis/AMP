package amp.topology.core.factory.rmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.Queue;
import amp.eventing.ISerializer;
import amp.topology.core.factory.impl.ContextMarshallingFactory;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.rmq.ModelUtils;
import amp.topology.rmq.repo.QueueRepository;

public class RmqQueueFactory extends ContextMarshallingFactory<Queue, RmqCompositeKey> {

	private static final Logger logger = LoggerFactory.getLogger(RmqQueueFactory.class);
	
	QueueRepository queueRepository;
	
	public RmqQueueFactory(ISerializer serializer, QueueRepository queueRepository) {
		super(serializer);
		
		this.queueRepository = queueRepository;
	}

	@Override
	public Class<RmqCompositeKey> getReferenceClass() {
		
		return RmqCompositeKey.class;
	}

	@Override
	public Queue build(RmqCompositeKey reference) {
		
		Queue queue = null;
		
		try {
			
			queue = ModelUtils.transform(
				queueRepository.get(reference.getCluster(), reference.getVhost(), reference.getName()));
			
		} catch (ClusterDoesntExistException cdee){
			
			logger.error("Cluster doesn't exist: {}", cdee);
		}
		
		return queue;
	}
}
