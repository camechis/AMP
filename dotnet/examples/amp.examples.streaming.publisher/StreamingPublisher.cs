using amp.eventing.streaming;
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

            IList<string> streamMessages = new List<string>();
            streamMessages.Add("I am");
            streamMessages.Add("the very ");
            streamMessages.Add("model of ");
            streamMessages.Add("a Modern ");
            streamMessages.Add("Major-General ");
            streamMessages.Add("I've information ");
            streamMessages.Add("vegetable, animal, ");
            streamMessages.Add("and mineral, ");
            streamMessages.Add("I know the kings of England, ");
            streamMessages.Add("and I quote the fights historical, ");
            streamMessages.Add("From Marathon to Waterloo, ");
            streamMessages.Add("in order categorical; ");

            IEventStream stream = streamingEventBus.CreateStream(typeof(string).FullName);
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
