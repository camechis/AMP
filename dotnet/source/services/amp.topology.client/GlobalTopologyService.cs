using System;
using System.Runtime.Caching;


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
    public class GlobalTopologyService
    {
        private MemoryCache _cache;
        private ulong _secondsUntilCacheExpires;
        private IRoutingInfoRetreiver _routingRepo;
        private IFallbackRoutingInfoProvider _fallbackProvider;


        public GlobalTopologyService (IRoutingInfoRetreiver routingRetreiver, ulong secondsUntilCacheExpires)
        {
            _routingRepo = routingRetreiver;
            _secondsUntilCacheExpires = secondsUntilCacheExpires;

            this.Initialize ();
        }

        public GlobalTopologyService (
            IRoutingInfoRetreiver routingRetreiver, 
            ulong secondsUntilCacheExpires, 
            IFallbackRoutingInfoProvider fallbackProvider)
        {
            _routingRepo = routingRetreiver;
            _secondsUntilCacheExpires = secondsUntilCacheExpires;
            _fallbackProvider = fallbackProvider;

            this.Initialize ();
        }


        protected void Initialize()
        {
            _cache = new MemoryCache ("amp.topology.client.cache");
        }
    }
}

