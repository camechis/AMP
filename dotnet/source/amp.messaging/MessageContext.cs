using System.Collections.Generic;
using cmf.bus;

namespace amp.messaging
{
    public class MessageContext
    {
        public enum Directions { In, Out }


        public Directions Direction { get; protected set; }
        public Envelope Envelope { get; set; }
        public object Message { get; set; }
        public IDictionary<string, object> Properties { get; set; }

        public object this[string key]
        {
            get
            {
                object value = null;

                if (this.Properties.ContainsKey(key))
                {
                    value = this.Properties[key];
                }

                return value;
            }
            set
            {
                this.Properties[key] = value;
            }
        }


        public MessageContext(Directions direction)
        {
            this.Direction = direction;
            this.Properties = new Dictionary<string, object>();
        }

        public MessageContext(Directions direction, Envelope envelope)
            : this(direction)
        {
            this.Envelope = envelope;
        }

        public MessageContext(Directions direction, Envelope envelope, object message)
            : this(direction, envelope)
        {
            this.Message = message;
        }
    }
}
