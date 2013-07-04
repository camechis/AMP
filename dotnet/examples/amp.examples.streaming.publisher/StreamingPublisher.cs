using amp.eventing.streaming;
using amp.examples.streaming.common;
using cmf.eventing.patterns.streaming;
using Spring.Context;
using Spring.Context.Support;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace amp.examples.streaming.publisher
{
    public class StreamingPublisher
    {
        public static void Main(string[] args)
        {
            IApplicationContext springContainer = ContextRegistry.GetContext();

            IStandardStreamingEventBus streamingEventBus = springContainer.GetObject(
                typeof(IStandardStreamingEventBus).FullName)
                as IStandardStreamingEventBus;

            IList<object> streamMessages = new List<object>();
            streamMessages.Add(new ModernMajorGeneralMessage("I am "));
            streamMessages.Add(new ModernMajorGeneralMessage("the very "));
            streamMessages.Add(new ModernMajorGeneralMessage("model of "));
            streamMessages.Add(new ModernMajorGeneralMessage("a Modern "));
            streamMessages.Add(new ModernMajorGeneralMessage("Major-General "));
            streamMessages.Add(new ModernMajorGeneralMessage("I've information "));
            streamMessages.Add(new ModernMajorGeneralMessage("vegetable, animal, "));
            streamMessages.Add(new ModernMajorGeneralMessage("and mineral, "));
            streamMessages.Add(new ModernMajorGeneralMessage("I know the kings of England, "));
            streamMessages.Add(new ModernMajorGeneralMessage("and I quote the fights historical, "));
            streamMessages.Add(new ModernMajorGeneralMessage("From Marathon to Waterloo, "));
            streamMessages.Add(new ModernMajorGeneralMessage("in order categorical; "));


            IEventStream stream = streamingEventBus.CreateStream(typeof(ModernMajorGeneralMessage).FullName);
            stream.BatchLimit = 2;

            foreach (object message in streamMessages)
            {
                stream.Publish(message);
            }
            stream.Dispose();
            
            Environment.Exit(0);
        }
    }
}
