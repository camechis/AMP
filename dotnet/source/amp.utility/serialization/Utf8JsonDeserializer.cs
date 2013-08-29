using System.IO;
using System.Text;
using log4net;
using Newtonsoft.Json;

namespace amp.utility.serialization
{
    public class Utf8JsonDeserializer<T> : IDeserializer<T> 
    {
        protected static readonly ILog Log = LogManager.GetLogger(typeof(Utf8JsonDeserializer<T>));


        public T Deserialize(byte[] routingBytes)
        {
            T routing;

            using (MemoryStream buffer = new MemoryStream(routingBytes))
            {
                routing = this.DeserializeRouting(buffer);
                buffer.Close();
            }

            return routing;
        }

        public T Deserialize(Stream routingStream)
        {
            T routing;

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

        public T DeserializeRouting(MemoryStream buffer)
        {
            // convert the buffer into an UTF-8 encoded string
            string jsonUtf8 = Encoding.UTF8.GetString(buffer.ToArray());

            // log the string for debugging
            Log.DebugFormat("Deserializing the following string into {1}: {0}", jsonUtf8, typeof(T));

            // use Json.NET to deserialize the JSON string
            return JsonConvert.DeserializeObject<T>(jsonUtf8);
        }

        public void Dispose()
        {
        }
    }
}
