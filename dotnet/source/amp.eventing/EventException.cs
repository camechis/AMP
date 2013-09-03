using System;
using System.Runtime.Serialization;

namespace amp.eventing
{
    public class EventException : Exception
    {
        public EventException()
        {
        }

        public EventException(string message) : base(message)
        {
        }

        public EventException(string message, Exception innerException) : base(message, innerException)
        {
        }

        protected EventException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}
