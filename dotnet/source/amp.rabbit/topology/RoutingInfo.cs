using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.rabbit.topology
{
    public class RoutingInfo
    {
        public IEnumerable<RouteInfo> Routes { get; set; }


        public RoutingInfo()
        {
            this.Routes = new List<RouteInfo>();
        }

        public RoutingInfo(IEnumerable<RouteInfo> routes)
        {
            this.Routes = routes;
        }
    }
}
