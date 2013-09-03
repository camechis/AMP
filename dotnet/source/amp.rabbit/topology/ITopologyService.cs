using System.Collections.Generic;

namespace amp.rabbit.topology
{
    public interface ITopologyService
    {
        RoutingInfo GetRoutingInfo(IDictionary<string, string> routingHints);
    }
}
