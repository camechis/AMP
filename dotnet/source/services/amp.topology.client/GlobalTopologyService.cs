using System;
using System.Collections.Generic;
using System.Runtime.Caching;

using cmf.bus;

using amp.rabbit.topology;


namespace amp.topology.client
{
    /// <summary>
    /// An implementation of ITopologyService that utilizes a central
    /// configuration and management system for topology, called
    /// the "Global Topology Service".  This implementation relies on 
    /// an external source to provide routing info.  If the routing info
    /// is found, it is cached for some interval.  If not, the Service will
    /// default to a "fallback" configuration.
    /// </summary>
    /// <author>John Ruiz (Berico Technologies)</author>
    public class GlobalTopologyService : ITopologyService, IDisposable
    {
        private MemoryCache _cache;
        private readonly object _cacheLock = new object();
        private readonly ulong _secondsUntilCacheExpires;
        private readonly IRoutingInfoRetreiver _routingRepo;
        private readonly IFallbackRoutingInfoProvider _fallbackProvider;


        public GlobalTopologyService(IRoutingInfoRetreiver routingRetreiver, ulong secondsUntilCacheExpires)
        {
            _routingRepo = routingRetreiver;
            _secondsUntilCacheExpires = secondsUntilCacheExpires;

            this.Initialize();
        }

        public GlobalTopologyService(
            IRoutingInfoRetreiver routingRetreiver,
            ulong secondsUntilCacheExpires,
            IFallbackRoutingInfoProvider fallbackProvider)
        {
            _routingRepo = routingRetreiver;
            _secondsUntilCacheExpires = secondsUntilCacheExpires;
            _fallbackProvider = fallbackProvider;

            this.Initialize();
        }


        protected virtual void Initialize()
        {
            _cache = new MemoryCache("amp.topology.client.cache");
        }


        public RoutingInfo GetRoutingInfo(IDictionary<string, string> routingHints)
        {
            // the routing we'll return
            RoutingInfo routing = null;


            // get the topic from the routing hints
            string topic = routingHints[EnvelopeHeaderConstants.MESSAGE_TOPIC];

            // guard clause -- make sure we have a topic
            if (null == topic) throw new ArgumentException("Cannot route a message with no topic");


            // try to get routing information from our cache
            // if no cached routing, use the injected info retriever
            routing = this.GetCachedRouting(topic) ?? _routingRepo.RetrieveRoutingInfo(topic);

            // if we still have no routing, ask the fallback provider
            if (routing.IsNullOrEmpty()) routing = _fallbackProvider.GetFallbackRoute(topic);

            // if even the fallback provider can't help us, we're done
            if (routing.IsNullOrEmpty()) throw new RoutingInfoNotFoundException("No routing for: " + topic);


            // otherwise, we have routing, and we should cache it
            this.AddCachedRouting(topic, routing);


            // finally, return the routing
            return routing;
        }

        public void AddCachedRouting(string topic, RoutingInfo routing)
        {
            // the item we're going to cache
            CacheItem item = new CacheItem(topic, routing);

            // the policy that manages the item in cache
            CacheItemPolicy policy = new CacheItemPolicy();
            policy.AbsoluteExpiration = DateTimeOffset.Now.AddSeconds(_secondsUntilCacheExpires);

            lock (_cacheLock)
            {
                // the add call returns true if insertion succeeded, or false 
                // if there is an already an entry in the cache that has the 
                // same key as item.  But why would I care?  I don't want to 
                // extend the cache time-to-live, so I'm happy with either.
                _cache.Add(item, policy);
            }
        }

        public RoutingInfo GetCachedRouting(string topic)
        {
            RoutingInfo routing = null;

            lock (_cacheLock)
            {
                routing = _cache.Get(topic) as RoutingInfo;
            }

            return routing;
        }

        public void Dispose()
        {
            // kill the cache
            lock (_cacheLock)
            {
                _cache.Trim(100);
            }

            // dispose the injected resources
            _routingRepo.Dispose();
            _fallbackProvider.Dispose();
        }
    }
}

