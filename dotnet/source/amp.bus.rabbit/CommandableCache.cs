using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

namespace amp.bus.rabbit
{
    public class CommandableCache : IRoutingInfoCache
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(CommandableCache));

        private Cache<String, RoutingInfo> routingInfoCache;
        private object cacheLock = new object();
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
                Log.warn("Failed to subscribe for Routing Cache Bust commands - the cache cannot be remotely commanded.", cex);
            }
        }


        public RoutingInfo GetIfPresent(String topic) {

            // I don't think we should lock this call, otherwise we'll lock everytime we try to read the cache, which
            // is going to kill performance.  Instead, I've made the cache volatile so that - at the very least -
            // this read will see the latest value.
            return routingInfoCache.getIfPresent(topic);
        }

        public void Put(String topic, RoutingInfo routingInfo) {

            this.cacheLock.lock();

            try {
                this.routingInfoCache.put(topic, routingInfo);
            }
            finally {
                this.cacheLock.unlock();
            }
        }

        public void Dispose() {
            this.commandReceiver.Dispose();
        }
    }
}
