using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Caching;
using System.Text;

using Common.Logging;

using amp.commanding;
using amp.rabbit.topology;

namespace amp.bus.rabbit
{
    public class CommandableCache : IRoutingInfoCache
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(CommandableCache));

        private readonly MemoryCache _routingInfoCache;
        private readonly int _cacheExpiryInSeconds;
        private readonly object _cacheLock = new object();
        private readonly ICommandReceiver _commandReceiver;
        

        public CommandableCache(ICommandReceiver commandReceiver, int cacheExpiryInSeconds)
        {
            _routingInfoCache = new MemoryCache("amp.bus.rabbit.cache");
            _commandReceiver = commandReceiver;
            _cacheExpiryInSeconds = cacheExpiryInSeconds;

            try {
                // subscribe for the command to burst the routing cache.  Pass a cache
                // reference into the cache buster that handles incoming BurstRoutingCache commands
                _commandReceiver.ReceiveCommand(new RoutingCacheBuster(_routingInfoCache, _cacheLock, _cacheExpiryInSeconds));
            }
            catch (CommandException cex) {
                Log.Warn("Failed to subscribe for Routing Cache Bust commands - the cache cannot be remotely commanded.", cex);
            }
        }


        public RoutingInfo GetIfPresent(String topic) {

            // I don't think we should lock this call, otherwise we'll lock everytime we try to read the cache, which
            // is going to kill performance.  The worst that happens is we make an extraneous call to the GTS.
            return _routingInfoCache.Get(topic) as RoutingInfo;
        }

        public void Put(String topic, RoutingInfo routingInfo)
        {
            lock (_cacheLock)
            {
                // creating a new cache policy each time is annoying, but necessary
                // see: http://stackoverflow.com/questions/16972641/expiring-a-cached-item-via-cacheitempolicy-in-net-memorycache
                CacheItemPolicy expirationPolicy = new CacheItemPolicy()
                {
                    AbsoluteExpiration = new DateTimeOffset(DateTime.UtcNow.AddSeconds(_cacheExpiryInSeconds))
                };

                _routingInfoCache.Add(new CacheItem(topic, routingInfo), expirationPolicy);
            }
        }

        public void Dispose()
        {
            try { _commandReceiver.Dispose(); }
            catch { }
        }
    }
}
