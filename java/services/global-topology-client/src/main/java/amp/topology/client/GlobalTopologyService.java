package amp.topology.client;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import cmf.bus.EnvelopeHeaderConstants;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.rabbit.topology.ITopologyService;
import amp.bus.rabbit.topology.RoutingInfo;


/**
 * An implementation of ITopologyService that utilizes a central
 * configuration and management system for topology, called
 * the "Global Topology Service".  This implementation relies on 
 * an external source to provide routing info.  If the routing info
 * is found, it is cached for some interval.  If not, the Service will
 * default to a "fallback" configuration.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class GlobalTopologyService implements ITopologyService {

	private static final Logger logger = LoggerFactory.getLogger(GlobalTopologyService.class);
	
	public static long CACHE_EXPIRY_TIME_IN_SECONDS = 1000;

    Logger log;

	Cache<String, RoutingInfo> routingInfoCache;
	
	IRoutingInfoRetriever routingInfoRetriever;
	
	FallbackRoutingInfoProvider fallbackProvider = null;
	
	public GlobalTopologyService(IRoutingInfoRetriever routingInfoRetriever){
		
		this(routingInfoRetriever, CACHE_EXPIRY_TIME_IN_SECONDS);
	}
	
	public GlobalTopologyService(
		IRoutingInfoRetriever routingInfoRetriever, 
		long cacheExpiryTime){

        this.log = LoggerFactory.getLogger(this.getClass());

		this.routingInfoRetriever = routingInfoRetriever;
		
		this.routingInfoCache = 
			CacheBuilder
				.newBuilder()
				.expireAfterWrite(cacheExpiryTime, TimeUnit.SECONDS)
				.build();
	}
	
	public GlobalTopologyService(
		IRoutingInfoRetriever routingInfoRetriever, 
		long cacheExpiryTime, 
		FallbackRoutingInfoProvider fallbackProvider){

		this(routingInfoRetriever, cacheExpiryTime);
		this.fallbackProvider = fallbackProvider;
	}

	@Override
	public RoutingInfo getRoutingInfo(Map<String, String> routingHints) {
		
		String topic = routingHints.get(EnvelopeHeaderConstants.MESSAGE_TOPIC);
		
		logger.info("Getting routing info for topic: {}", topic);
		
		RoutingInfo routingInfo = this.routingInfoCache.getIfPresent(topic);
		
		if (routingInfo == null){
			
			logger.info("Routing info not in cache, going to the retriever.");
			
			routingInfo = this.routingInfoRetriever.retrieveRoutingInfo(topic);
			
			if (routingInfoAbsentOrNotValid(routingInfo)
				&& this.fallbackProvider != null) {
				
				routingInfo = this.fallbackProvider.getFallbackRoute(topic);
			}
			
			// If the RoutingInfo is still null, end this program's life!
			if (routingInfo == null){
				
				throw new RoutingInfoNotFoundException(topic);
			}
			
			this.routingInfoCache.put(topic, routingInfo);
		}
		else {
			
			logger.debug("Found routing info in the cache.");
		}
		
		return routingInfo;
	}

	/**
	 * Determine if the routing info is absent or invalid (i.e. no routes).
	 * @param routingInfo RoutingInfo returned from Retreiver.
	 * @return true is Absent or Invalid.
	 */
	protected boolean routingInfoAbsentOrNotValid(RoutingInfo routingInfo){
		
		logger.debug("Determining if Routing Info is absent or invalid.");
		
		if (routingInfo != null){
			
			logger.debug("Routing info is not null.");
			
			if (routingInfo.getRoutes() != null){
				
				logger.debug("Routes are not null.");
				
				if (routingInfo.getRoutes().iterator().hasNext()){
					
					logger.debug("Routes has next.");
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void dispose() {}
}
