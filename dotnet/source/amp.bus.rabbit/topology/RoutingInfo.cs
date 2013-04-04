using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.bus.rabbit.topology
{
    public class RoutingInfo
    {
        public IEnumerable<RouteInfo> Routes { get; private set; }


        public RoutingInfo(IEnumerable<RouteInfo> routes)
            : this()
        {
            this.Routes = routes;
        }
    }
}
