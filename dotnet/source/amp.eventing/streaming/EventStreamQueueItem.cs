using amp.messaging;
using cmf.bus;

namespace amp.eventing.streaming
{
    /// <summary>
    /// Contains the elements needed to send an event in an event stream.
    /// </summary>
    public class EventStreamQueueItem
    {
        private readonly MessageContext _eventContext;

        public EventStreamQueueItem(MessageContext eventContext)
        {
            _eventContext = eventContext;
        }

        public Envelope Envelope { get { return _eventContext.Envelope; } }

        public MessageContext MessageContext { get { return _eventContext; } }
    }
}
