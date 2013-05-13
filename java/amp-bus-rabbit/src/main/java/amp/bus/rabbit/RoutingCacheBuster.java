package amp.bus.rabbit;


import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.google.common.cache.Cache;

import amp.commanding.ICommandHandler;
import amp.commands.BurstRoutingCacheCommand;
import amp.rabbit.topology.RoutingInfo;


/**
 * Created with IntelliJ IDEA.
 * User: John
 * Date: 5/8/13
 */
public class RoutingCacheBuster implements ICommandHandler<BurstRoutingCacheCommand> {

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

        this.cacheLock.lock();

        try {
            // no matter what, burst the cache
            this.routingInfoCache.invalidateAll();

            // however, the command may (optionally) carry new routing.
            Map<String, RoutingInfo> newRouting = command.getNewRoutingInfo();

            // if it does, populate the cache with it
            if (null != newRouting) {
                this.routingInfoCache.putAll(newRouting);
            }
        }
        finally {
            this.cacheLock.unlock();
        }
    }
}
