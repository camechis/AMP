using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.commanding
{
    public class CommandAttribute : Attribute
    {
        public string Topic { get; set; }
        public string Type { get; set; }


        public CommandAttribute(string topic = null, string type = null)
        {
            this.Topic = topic;
            this.Type = type;
        }
    }
}
