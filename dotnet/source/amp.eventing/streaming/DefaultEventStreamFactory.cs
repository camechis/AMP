using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.eventing.streaming
{
    public class DefaultEventStreamFactory : IEventStreamFactory 
    {
        private DefaultStreamingBus _eventBus;
        private string _topic;

        public DefaultStreamingBus DefaultStreamingBus { set { _eventBus = value; } }
        public string Topic { set { _topic = value; } }

        public IEventStream GenerateEventStream()
        {
            return new DefaultEventStream(_eventBus, _topic);
        }
    }
}
