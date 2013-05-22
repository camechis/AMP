using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using amp.rabbit.topology;

namespace amp.commands
{
    public class BurstRoutingCacheCommand
    {
        public IDictionary<string, RoutingInfo> NewRoutingInfo { get; set; }


        public BurstRoutingCacheCommand()
        {
            this.NewRoutingInfo = new Dictionary<string, RoutingInfo>();
        }

        public BurstRoutingCacheCommand(IDictionary<string, RoutingInfo> routingInfo)
        {
            this.NewRoutingInfo = routingInfo;
        }
    }
}
