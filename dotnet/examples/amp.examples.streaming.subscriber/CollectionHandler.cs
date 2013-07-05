using amp.examples.streaming.common;
using cmf.eventing.patterns.streaming;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace amp.examples.streaming.subscriber
{
    public class CollectionHandler : IStreamingCollectionHandler<ModernMajorGeneralMessage>
    {
        private Action<string, int> _notifier;

        public object HandleCollection(IEnumerable<IStreamingEventItem<ModernMajorGeneralMessage>> events, IDictionary<string, string> headers)
        {
            Console.WriteLine("Received a collection from AMPere of size: " + events.Count());

            foreach(IStreamingEventItem<ModernMajorGeneralMessage> eventItem in events)
            {
                Console.WriteLine(string.Format("Event Item: SequenceId => {0}, Position => {1}, IsLast => {2}, Content => {3}",
                    eventItem.SequenceId, eventItem.Position, eventItem.IsLast, eventItem.Event.Content));
            }

            Console.WriteLine("Processing complete.");
            return null;
        }
        
        public Action<string, int> Progress
        {
            get
            {
                return _notifier;
            }
            set
            {
                _notifier = value;
            }
        }

        public object Handle(object ev, IDictionary<string, string> headers)
        {
            Console.WriteLine("Handle called");
            return null;
        }

        public object HandleFailed(cmf.bus.Envelope env, Exception ex)
        {
            Console.WriteLine("HandleFailed called");
            return null;
        }

        public string Topic
        {
            get { return typeof(ModernMajorGeneralMessage).FullName; }
        }
    }
}
