package amp.eventing.streaming;

import cmf.bus.Envelope;

import static cmf.eventing.patterns.streaming.StreamingEnvelopeConstants.*;

public class StreamingEnvelopeHelper {

    public static Envelope buildStreamingEnvelope(String sequenceId, int position, String isLast) {
        Envelope envelope = new Envelope();
        envelope.setHeader(SEQUENCE_ID, sequenceId);
        envelope.setHeader(POSITION, position + "");
        envelope.setHeader(IS_LAST, isLast);
        return envelope;
    }


}
