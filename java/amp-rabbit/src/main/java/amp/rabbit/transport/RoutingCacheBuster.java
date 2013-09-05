package amp.rabbit.transport;


import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.google.common.cache.Cache;

import amp.commanding.ICommandHandler;
import amp.rabbit.commands.BurstRoutingCacheCommand;
import amp.rabbit.topology.RoutingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: John
 * Date: 5/8/13
 */
public class RoutingCacheBuster implements ICommandHandler<BurstRoutingCacheCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingCacheBuster.class);

    private Cache<String, RoutingInfo> routingInfoCache;
    private Lock cacheLock;


    public RoutingCacheBuster(Cache<String, RoutingInfo> routingInfoCache, Lock cacheLock) {
        this.routingInfoCache = routingInfoCache;
        this.cacheLock = cacheLock;
    }


    @Override
    public Class<BurstRoutingCacheCommand> getCommandType() {
        return BurstRoutingCacheCommand.class;
    }

    @Override
    public void handle(BurstRoutingCacheCommand command, Map<String, String> headers) {

        LOG.info("Received a command to burst the routing cache.");

        this.cacheLock.lock();

        try {
            // no matter what, burst the cache
            this.routingInfoCache.invalidateAll();
            LOG.debug("The routing cache has been invalidated.");

            // however, the command may (optionally) carry new routing.
            Map<String, RoutingInfo> newRouting = command.getNewRoutingInfo();

            // if it does, populate the cache with it
            if (null != newRouting) {
                LOG.debug("The cache burst command contained new routing - caching it.");
                this.routingInfoCache.putAll(newRouting);
            }
        }
        finally {
            this.cacheLock.unlock();
        }
    }
}
