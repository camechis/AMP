using System;
using amp.rabbit.topology;

namespace amp.topology.client
{
    /// <summary>
    /// Retrieve routing info for a topic.  We don't care how,  
    /// just do it!                                             
    /// 
    /// This is used to allow multiple protocols/implementations
    /// for retrieving route info by Global Topology Service.              
    /// </summary>
    /// <author>John Ruiz (Berico Technologies)</author>
    public interface IRoutingInfoRetreiver : IDisposable
    {
        RoutingInfo RetrieveRoutingInfo(String topic);
    }
}

