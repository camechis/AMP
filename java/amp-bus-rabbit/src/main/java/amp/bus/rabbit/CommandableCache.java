package amp.bus.rabbit;


import java.util.concurrent.TimeUnit;

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

    private Cache<String, RoutingInfo> routingInfoCache;
    private ICommandReceiver commandReceiver;


    public CommandableCache(ICommandReceiver commandReceiver, long cacheExpiryInSeconds) {

        commandReceiver = commandReceiver;

        routingInfoCache = CacheBuilder
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
}
