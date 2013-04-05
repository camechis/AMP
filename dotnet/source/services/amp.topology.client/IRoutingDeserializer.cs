using System;
using System.IO;

using amp.bus.rabbit.topology;


namespace amp.topology.client
{
    public interface IRoutingDeserializer : IDisposable
    {
        RoutingInfo DeserializeRouting(byte[] routingBytes);

        RoutingInfo DeserializeRouting(Stream routingStream);
    }
}
