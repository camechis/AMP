package amp.esp;


import cmf.bus.Envelope;

import java.util.Date;
import java.util.UUID;

public class WEUtils {

    // ===================
    public static byte[] getBody(Envelope envelope) {
        return envelope.getPayload();
    }

    public static Envelope setBody(Envelope envelope, byte[] bytes) {
        envelope.setPayload(bytes);
        return envelope;
    }



    // ===================
    public static String getx(Envelope envelope, String key) {
        return envelope.getHeader(key);
    }

    public static Envelope setx(Envelope envelope, String key, String value) {
        envelope.setHeader(key, value);
        return envelope;
    }

    // ===================
    public static String getEventType(Envelope envelope) {
        return getx(envelope, "EventType");
    }

    public static Envelope setEventType(Envelope envelope, String type) {
        return setx(envelope, "EventType", type);
    }

    // ===================
    public static String getReplyTo(Envelope envelope) {
        return getx(envelope, "ReplyTo");
    }

    public static Envelope setReplyTo(Envelope envelope, String replyTo) {
        return setx(envelope, "ReplyTo", replyTo);
    }

    // ===================
    public static String getTopic(Envelope envelope) {
        return getx(envelope, "Topic");
    }

    public static Envelope setTopic(Envelope envelope, String topic) {
        return setx(envelope, "Topic", topic);
    }

    // ===================
    public static String getId(Envelope envelope) {
        return getx(envelope, "Id");
    }

    public static Envelope setId(Envelope envelope, UUID symIdToRealId) {
        return setx(envelope, "Id", symIdToRealId.toString());

    }

    // ===================
    public static String getCorrelationId(Envelope envelope) {
        return getx(envelope, "CorrelationId");
    }

    public static Envelope setCorrelationId(Envelope envelope, UUID symIdToRealId) {
        return setx(envelope, "CorrelationId", symIdToRealId.toString());
    }


    // ===================
    // TODO check uses of timestamp (with goal toward removal)
    public static Date getTimestamp(Envelope envelope) {
        return new Date(Long.parseLong(getx(envelope, "Timestamp")));
    }

    public static Envelope setTimestamp(Envelope envelope, Date timestamp) {
        return setx(envelope, "Timestamp", Long.toString(timestamp.getTime()));
    }

}
