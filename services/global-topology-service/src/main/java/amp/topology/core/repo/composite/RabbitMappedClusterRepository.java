package amp.topology.core.repo.composite;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.model.Overview;
import amp.topology.core.model.Cluster;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.model.ClusterRegistration;
import amp.topology.core.repo.ClusterRegistrationRepository;
import amp.topology.core.repo.ClusterRepository;
import amp.topology.core.repo.rmq.ModelUtils;
import amp.topology.rmq.repo.ClusterInfoRepository;

public class RabbitMappedClusterRepository implements ClusterRepository {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(RabbitMappedClusterRepository.class);
	
	ClusterRegistrationRepository registrationRepository;
	ClusterInfoRepository clusterInfoRepository;
	
	public RabbitMappedClusterRepository(
		ClusterRegistrationRepository registrationRepository,
		ClusterInfoRepository clusterInfoRepository){
		
		this.registrationRepository = registrationRepository;
		this.clusterInfoRepository = clusterInfoRepository;
	}
	
	@Override
	public Collection<Cluster> all() {
		
		logger.debug("Getting all clusters.");
		
		Collection<ClusterRegistration> registrations = registrationRepository.all();
		
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		
		for (ClusterRegistration registration : registrations){
			
			try {
				
				Overview overview = clusterInfoRepository.getOverview(registration.getId());
				
				Cluster cluster = ModelUtils.transform(
					registration.getId(), registration.getDescription(), overview);
				
				clusters.add(cluster);
				
			} catch (ClusterDoesntExistException e) {
				
				logger.error("This shouldn't have happened! {}", e);
			}
		}
		
		return clusters;
	}

	@Override
	public Cluster get(String cluster) throws ClusterDoesntExistException {
		
		logger.debug("Getting Cluster with id {}", cluster);
		
		ClusterRegistration registration = registrationRepository.get(cluster);
		
		Overview overview = clusterInfoRepository.getOverview(cluster);
		
		return ModelUtils.transform(cluster, registration.getDescription(), overview);
	}
}
