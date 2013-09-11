using System;

namespace amp.messaging
{
    public class MessageAttribute : Attribute
    {
        public string Topic { get; set; }
        public string Type { get; set; }


        public MessageAttribute(string topic = null, string type = null)
        {
            this.Topic = topic;
            this.Type = type;
        }
    }
}
