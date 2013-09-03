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
    /// is not found, it will default to a "fallback" configuration.
    /// </summary>
    /// <author>John Ruiz (Berico Technologies)</author>
    public class GlobalTopologyService : ITopologyService, IDisposable
    {
        private readonly IRoutingInfoRetreiver _routingRepo;
        private readonly IFallbackRoutingInfoProvider _fallbackProvider;


        public GlobalTopologyService(IRoutingInfoRetreiver routingRetreiver)
        {
            _routingRepo = routingRetreiver;
        }

        public GlobalTopologyService(
            IRoutingInfoRetreiver routingRetreiver,
            IFallbackRoutingInfoProvider fallbackProvider)
            : this(routingRetreiver)
        {
            _fallbackProvider = fallbackProvider;
        }


        public RoutingInfo GetRoutingInfo(IDictionary<string, string> routingHints)
        {
            // the routing we'll return
            RoutingInfo routing = null;


            // get the topic from the routing hints
            string topic = routingHints[EnvelopeHeaderConstants.MESSAGE_TOPIC];

            // guard clause -- make sure we have a topic
            if (null == topic) throw new ArgumentException("Cannot route a message with no topic");


            // try to get routing information from the injected info retriever
            routing = _routingRepo.RetrieveRoutingInfo(topic);

            // if we have no routing, ask the fallback provider
            if (routing.IsNullOrEmpty()) routing = _fallbackProvider.GetFallbackRoute(topic);

            // if even the fallback provider can't help us, we're done
            if (routing.IsNullOrEmpty()) throw new RoutingInfoNotFoundException("No routing for: " + topic);


            // finally, return the routing
            return routing;
        }

        public void Dispose()
        {
            // dispose the injected resources
            try { _routingRepo.Dispose(); }
            catch { }

            try { _fallbackProvider.Dispose(); }
            catch { }
        }
    }
}

