using System.Collections.Generic;
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
