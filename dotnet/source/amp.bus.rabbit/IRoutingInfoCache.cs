using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using amp.rabbit.topology;

namespace amp.bus.rabbit
{
    public interface IRoutingInfoCache : IDisposable
    {
        RoutingInfo GetIfPresent(string topic);

        void Put(string topic, RoutingInfo routingInfo);
    }
}
