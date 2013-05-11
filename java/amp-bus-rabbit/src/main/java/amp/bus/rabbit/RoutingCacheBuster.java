package amp.bus.rabbit;


import java.util.Map;

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


    public RoutingCacheBuster(Cache<String, RoutingInfo> routingInfoCache) {
        this.routingInfoCache = routingInfoCache;
    }


    @Override
    public Class<BurstRoutingCacheCommand> getCommandType() {
        return BurstRoutingCacheCommand.class;
    }

    @Override
    public void handle(BurstRoutingCacheCommand command, Map<String, String> headers) {

        this.routingInfoCache.invalidateAll();
        Map<String, RoutingInfo> newRouting = command.getNewRoutingInfo();

        if (null != newRouting) {
            this.routingInfoCache.putAll(newRouting);
        }
    }
}
