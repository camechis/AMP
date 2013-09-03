using System;
using System.IO;
using System.Text;

using log4net;
using Newtonsoft.Json;

using amp.rabbit.topology;


namespace amp.topology.client
{
    public class Utf8JsonRoutingDeserializer : IRoutingDeserializer
    {
        protected static readonly ILog Log = LogManager.GetLogger(typeof(Utf8JsonRoutingDeserializer));


        public RoutingInfo DeserializeRouting(byte[] routingBytes)
        {
            RoutingInfo routing = null;

            using (MemoryStream buffer = new MemoryStream(routingBytes))
            {
                routing = this.DeserializeRouting(buffer);
                buffer.Close();
            }

            return routing;
        }

        public RoutingInfo DeserializeRouting(Stream routingStream)
        {
            RoutingInfo routing = null;

            using (MemoryStream buffer = new MemoryStream())
            {
                // using CopyTo because of a stackoverflow answer
                // http://stackoverflow.com/questions/221925/creating-a-byte-array-from-a-stream
                routingStream.CopyTo(buffer);
                routing = this.DeserializeRouting(buffer);
                buffer.Close();
            }

            return routing;
        }

        public RoutingInfo DeserializeRouting(MemoryStream buffer)
        {
            // convert the buffer into an UTF-8 encoded string
            string jsonUtf8 = Encoding.UTF8.GetString(buffer.ToArray());

            // log the string for debugging
            Log.DebugFormat("Deserializing the following string into RoutingInfo: {0}", jsonUtf8);

            // use Json.NET to deserialize the JSON string
            return JsonConvert.DeserializeObject<RoutingInfo>(jsonUtf8);
        }

        public void Dispose()
        {
        }
    }
}
