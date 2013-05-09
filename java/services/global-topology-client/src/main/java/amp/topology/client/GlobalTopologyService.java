package amp.topology.client;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import amp.commanding.CommandException;
import amp.commanding.ICommandReceiver;
import cmf.bus.EnvelopeHeaderConstants;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.topology.ITopologyService;
import amp.rabbit.topology.RoutingInfo;


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

    public static long CACHE_EXPIRY_TIME_IN_SECONDS = 1000;
	private static final Logger LOG = LoggerFactory.getLogger(GlobalTopologyService.class);

	Cache<String, RoutingInfo> routingInfoCache;
	IRoutingInfoRetriever routingInfoRetriever;
	FallbackRoutingInfoProvider fallbackProvider = null;
    ICommandReceiver commandReceiver;


	public GlobalTopologyService(IRoutingInfoRetriever routingInfoRetriever, ICommandReceiver commandReceiver){

        this.initialize(routingInfoRetriever, commandReceiver, CACHE_EXPIRY_TIME_IN_SECONDS, null);
	}
	
	public GlobalTopologyService(
		IRoutingInfoRetriever routingInfoRetriever,
        ICommandReceiver commandReceiver,
		long cacheExpiryTime){

		this.routingInfoRetriever = routingInfoRetriever;
        this.commandReceiver = commandReceiver;
    }

	public GlobalTopologyService(
		IRoutingInfoRetriever routingInfoRetriever,
        ICommandReceiver commandReceiver,
		long cacheExpiryTime, 
		FallbackRoutingInfoProvider fallbackProvider){

        this.initialize(routingInfoRetriever, commandReceiver, cacheExpiryTime, fallbackProvider);
	}



    public void initialize(
            IRoutingInfoRetriever routingInfoRetriever,
            ICommandReceiver commandReceiver,
            long cacheExpiryInSeconds,
            FallbackRoutingInfoProvider fallbackProvider) {

        this.routingInfoRetriever = routingInfoRetriever;
        this.commandReceiver = commandReceiver;
        this.fallbackProvider = fallbackProvider;
        this.routingInfoCache =
                CacheBuilder
                        .newBuilder()
                        .expireAfterWrite(cacheExpiryInSeconds, TimeUnit.SECONDS)
                        .build();

        try {
            // subscribe for the command to burst the routing cache
            this.commandReceiver.onCommandReceived(new RoutingCacheBuster(this.routingInfoCache));
        }
        catch (CommandException cex) {
            LOG.warn("Failed to subscribe for Routing Cache Bust commands - the cache cannot be remotely commanded.", cex);
        }
    }

	@Override
	public RoutingInfo getRoutingInfo(Map<String, String> routingHints) {
		
		String topic = routingHints.get(EnvelopeHeaderConstants.MESSAGE_TOPIC);
		
		LOG.info("Getting routing info for topic: {}", topic);
		
		RoutingInfo routingInfo = this.routingInfoCache.getIfPresent(topic);
		
		if (routingInfo == null){
			
			LOG.info("Routing info not in cache, going to the retriever.");
			
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
			
			LOG.debug("Found routing info in the cache.");
		}
		
		return routingInfo;
	}

	@Override
	public void dispose() {}



    /**
     * Determine if the routing info is absent or invalid (i.e. no routes).
     * @param routingInfo RoutingInfo returned from Retreiver.
     * @return true is Absent or Invalid.
     */
    protected boolean routingInfoAbsentOrNotValid(RoutingInfo routingInfo){

        LOG.debug("Determining if Routing Info is absent or invalid.");

        if (routingInfo != null){

            LOG.debug("Routing info is not null.");

            if (routingInfo.getRoutes() != null){

                LOG.debug("Routes are not null.");

                if (routingInfo.getRoutes().iterator().hasNext()){

                    LOG.debug("Routes has next.");

                    return false;
                }
            }
        }

        return true;
    }
}
