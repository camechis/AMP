using System;
using amp.bus.rabbit.topology;


namespace amp.topology.client
{
    public interface IFallbackRoutingInfoProvider
    {
        RoutingInfo getFallbackRoute(String topic);
    }
}

