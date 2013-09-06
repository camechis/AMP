using System;
using System.Runtime.Serialization;

namespace amp.commanding
{
    public class CommandException : Exception
    {
        public CommandException() : base()
        {
        }

        public CommandException(string message) : base(message)
        {
        }

        public CommandException(string message, Exception innerException)
            : base(message, innerException)
        {
        }

        protected CommandException(SerializationInfo info, StreamingContext context)
            : base(info, context)
        {
        }
    }
}
