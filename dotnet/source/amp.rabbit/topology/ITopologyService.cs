using System.Collections.Generic;
using System;

namespace amp.rabbit.topology
{
    public interface ITopologyService : IDisposable
    {
        RoutingInfo GetRoutingInfo(IDictionary<string, string> routingHints);
    }
}
