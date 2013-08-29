using System;
using System.Runtime.Serialization;


namespace amp.topology.client
{
    public class RoutingInfoNotFoundException : Exception
    {
        public RoutingInfoNotFoundException()
        {
        }

        public RoutingInfoNotFoundException(string message)
            : base(message)
        {
        }

        public RoutingInfoNotFoundException(string message, Exception innerException)
            : base(message, innerException)
        {
        }

        protected RoutingInfoNotFoundException(SerializationInfo info, StreamingContext context)
            : base(info, context)
        {
        }
    }
}
