using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Caching;
using System.Text;

using Common.Logging;

using amp.commanding;
using amp.commands;
using amp.rabbit.topology;

namespace amp.bus.rabbit
{
    public class RoutingCacheBuster : TypedCommandHandler<BurstRoutingCacheCommand>
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(RoutingCacheBuster));

        private readonly MemoryCache _routingInfoCache;
        private readonly int _cacheExpiryInSeconds;
        private readonly object _cacheLock;


        public RoutingCacheBuster(MemoryCache routingInfoCache, object cacheLock, int cacheExpiryInSeconds)
        {
            _routingInfoCache = routingInfoCache;
            _cacheLock = cacheLock;
            _cacheExpiryInSeconds = cacheExpiryInSeconds;

            this.Handler = this.HandleCommand;
        }


        public void HandleCommand(BurstRoutingCacheCommand command, IDictionary<string, string> headers)
        {
            Log.Info("Received a command to burst the routing cache.");

            lock (_cacheLock)
            {
                // no matter what, burst the cache
                // see: http://stackoverflow.com/questions/8043381/how-do-i-clear-a-system-runtime-caching-memorycache
                _routingInfoCache
                    .Select(entry => entry.Key) // from all cached items, select the keys
                    .ToList() // convert to a list (gives access to ForEach)
                    .ForEach(key => _routingInfoCache.Remove(key)); // and remove all keys from the cache

                Log.Debug("The routing cache has been invalidated.");

                // however, the command may (optionally) carry new routing.
                IDictionary<String, RoutingInfo> newRouting = command.NewRoutingInfo;

                // if it does, populate the cache with it
                if ( (null != newRouting) && (!newRouting.Any()) )
                {
                    Log.Debug("The cache burst command contained new routing - caching it.");
                    
                    newRouting.ToList().ForEach(entry =>
                    {
                        // creating a new cache policy each time is annoying, but necessary
                        // see: http://stackoverflow.com/questions/16972641/expiring-a-cached-item-via-cacheitempolicy-in-net-memorycache
                        var expirationPolicy = new CacheItemPolicy()
                        {
                            AbsoluteExpiration = new DateTimeOffset(DateTime.UtcNow.AddSeconds(_cacheExpiryInSeconds))
                        };

                        _routingInfoCache.Add(new CacheItem(entry.Key, entry.Value), expirationPolicy);
                    });
                }
            }
        }
    }
}
