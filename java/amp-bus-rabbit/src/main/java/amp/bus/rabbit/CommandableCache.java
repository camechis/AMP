package amp.bus.rabbit;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.commanding.ICommandReceiver;
import amp.commanding.CommandException;
import amp.rabbit.topology.RoutingInfo;


/**
 * Created with IntelliJ IDEA.
 * User: John
 * Date: 5/11/13
 */
public class CommandableCache implements IRoutingInfoCache {

    private static final Logger LOG = LoggerFactory.getLogger(CommandableCache.class);

    private volatile Cache<String, RoutingInfo> routingInfoCache;
    private Lock cacheLock;
    private ICommandReceiver commandReceiver;


    public CommandableCache(ICommandReceiver commandReceiver, long cacheExpiryInSeconds) {

        this.commandReceiver = commandReceiver;

        this.routingInfoCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(cacheExpiryInSeconds, TimeUnit.SECONDS)
                .build();

        this.cacheLock = new ReentrantLock();

        try {
            // subscribe for the command to burst the routing cache.  Pass a cache
            // reference into the cache buster that handles incoming BurstRoutingCache commands
            this.commandReceiver.onCommandReceived(new RoutingCacheBuster(this.routingInfoCache, cacheLock));
        }
        catch (CommandException cex) {
            LOG.warn("Failed to subscribe for Routing Cache Bust commands - the cache cannot be remotely commanded.", cex);
        }
    }


    @Override
    public RoutingInfo getIfPresent(String topic) {

        // I don't think we should lock this call, otherwise we'll lock everytime we try to read the cache, which
        // is going to kill performance.  Instead, I've made the cache volatile so that - at the very least -
        // this read will see the latest value.
        return routingInfoCache.getIfPresent(topic);
    }

    @Override
    public void put(String topic, RoutingInfo routingInfo) {

        this.cacheLock.lock();

        try {
            this.routingInfoCache.put(topic, routingInfo);
        }
        finally {
            this.cacheLock.unlock();
        }
    }

    @Override
    public void dispose() {
        this.commandReceiver.dispose();
    }
}
