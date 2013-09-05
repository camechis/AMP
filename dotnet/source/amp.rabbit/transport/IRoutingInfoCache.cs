using System;
using amp.rabbit.topology;

namespace amp.rabbit.transport
{
    public interface IRoutingInfoCache : IDisposable
    {
        RoutingInfo GetIfPresent(string topic);

        void Put(string topic, RoutingInfo routingInfo);
    }
}
