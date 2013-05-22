using System;
using amp.rabbit.topology;


namespace amp.topology.client
{
    public interface IFallbackRoutingInfoProvider : IDisposable
    {
        RoutingInfo GetFallbackRoute(String topic);
    }
}

