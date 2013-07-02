using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.eventing.patterns.streaming;

using SEC = amp.eventing.streaming.StreamingEnvelopeConstants;

namespace amp.eventing.streaming
{
    /// <summary>
    /// Contains the elements needed to receive an event from an event stream.
    /// This allows each disrete event pulled from a subscribed event stream to 
    /// contain the necessary state that a subscriber would need including the 
    /// sequenceId, position, and whether this event is the last in the published
    /// sequence
    /// </summary>
    public class StreamingEventItem<TEvent> : IStreamingEventItem<TEvent>
    {
        private readonly TEvent _streamEvent;
        private readonly IDictionary<string, string> _eventHeaders;

        public StreamingEventItem(TEvent streamEvent, IDictionary<string, string> eventHeaders)
        {
            _streamEvent = streamEvent;
            _eventHeaders = eventHeaders;
        }

        public TEvent Event 
        { 
            get 
            {
                return _streamEvent;
            } 
        }

        public Guid SequenceId 
        {
            get
            { 
                return Guid.Parse(_eventHeaders[SEC.SEQUENCE_ID]);
            }
        }

        public int Position
        {
            get 
            {
                return int.Parse(_eventHeaders[SEC.POSITION]);
            }
        }

        public bool IsLast
        {
            get 
            {
                return bool.Parse(_eventHeaders[SEC.IS_LAST]); 
            }
        }

        public IDictionary<string, string> EventHeaders
        {
            get 
            {
                return _eventHeaders;
            }
        }

    }
}
