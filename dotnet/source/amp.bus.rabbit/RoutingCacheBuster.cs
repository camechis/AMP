using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

namespace amp.bus.rabbit
{
    public class RoutingCacheBuster : ICommandHandler<BurstRoutingCacheCommand>
    {
        private static final Logger LOG = LoggerFactory.getLogger(RoutingCacheBuster.class);

        private Cache<String, RoutingInfo> routingInfoCache;
        private Lock cacheLock;


        public RoutingCacheBuster(Cache<String, RoutingInfo> routingInfoCache, Lock cacheLock) {
            this.routingInfoCache = routingInfoCache;
            this.cacheLock = cacheLock;
        }


        public Class<BurstRoutingCacheCommand> getCommandType() {
            return BurstRoutingCacheCommand.class;
        }

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
}
