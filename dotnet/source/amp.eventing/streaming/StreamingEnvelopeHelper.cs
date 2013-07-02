using cmf.bus;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using SEC = amp.eventing.streaming.StreamingEnvelopeConstants;

namespace amp.eventing.streaming
{
    public class StreamingEnvelopeHelper
    {
        public static Envelope BuildStreamingEnvelope(string sequenceId, int position, string isLast) 
        {
            Envelope envelope = new Envelope();
            envelope.Headers.Add(SEC.SEQUENCE_ID, sequenceId);
            envelope.Headers.Add(SEC.POSITION, position + "");
            envelope.Headers.Add(SEC.IS_LAST, isLast);
            return envelope;
        }
    }
}
