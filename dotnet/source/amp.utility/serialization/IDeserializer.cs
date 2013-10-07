using System;
using System.IO;

namespace amp.utility.serialization
{
    public interface IDeserializer<T> : IDisposable
    {
        T Deserialize(byte[] routingBytes);

        T Deserialize(Stream routingStream);
    }
}
