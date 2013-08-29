using System;
using System.IO;


namespace amp.topology.client
{
    public interface IDeserializer<T> : IDisposable
    {
        T Deserialize(byte[] routingBytes);

        T Deserialize(Stream routingStream);
    }
}
