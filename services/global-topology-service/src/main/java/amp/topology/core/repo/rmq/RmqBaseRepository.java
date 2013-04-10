package amp.topology.core.repo.rmq;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.RabbitMgmtService;

/**
 * Adds some essential functionality all repos using the RabbitMQ
 * Management Console will need.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class RmqBaseRepository {

	Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	RmqManagementFactory managementFactory;
	
	public RmqBaseRepository(RmqManagementFactory managementFactory){
		
		this.managementFactory = managementFactory;
	}
	
	Collection<RabbitMgmtService> getManagers(String clusterId){
		
		return managementFactory.getInstances(clusterId);
	}
	
	/**
	 * Execute function against the supplied cluster, returning a value
	 * from the first Management Console to not throw an exception.
	 * 
	 * @param clusterId Id of the Cluster
	 * @param function Function to execute.
	 * @return Whatever value you were expecting.
	 */
	public <T> T execute(String clusterId, ManagementFunction<T> function){
		
		T result = null;
		
		Collection<RabbitMgmtService> managers = getManagers(clusterId);
		
		for (RabbitMgmtService manager : managers){
			
			try {
				
				result = function.perform(manager);
				
			} catch (Exception e) {
				
				logger.error("Problem executing against Management Endpoint: {}", e);
			}
		}
		
		return result;
	}
	
	/**
	 * Execute an action against the supplied cluster, returning nothing.
	 * 
	 * @param clusterId Id of the Cluster
	 * @param action Action to execute.
	 */
	public void execute(String clusterId, ManagementAction action){
		
		Collection<RabbitMgmtService> managers = getManagers(clusterId);
		
		for (RabbitMgmtService manager : managers){
			
			try {
				
				action.perform(manager);
				
			} catch (Exception e) {
				
				logger.error("Problem executing against Management Endpoint: {}", e);
			}
		}
	}
	
	/**
	 * Generic wrapper for executing against a Management Endpoint.  If anything goes
	 * wrong, feel free to throw an exception and we'll call the next endpoint.
	 * 
	 * @author Richard Clayton (Berico Technologies)
	 *
	 * @param <T> Return type.
	 */
	public interface ManagementFunction<T> {
		
		T perform(RabbitMgmtService mgmtService) throws Exception;
	}
	
	/**
	 * Generic wrapper for executing against a Management Endpoint.  If anything goes
	 * wrong, feel free to throw an exception and we'll call the next endpoint.  This
	 * version returns nothing.
	 * 
	 * @author Richard Clayton (Berico Technologies)
	 */
	public interface ManagementAction {
		
		void perform(RabbitMgmtService mgmtService) throws Exception;
	}
}