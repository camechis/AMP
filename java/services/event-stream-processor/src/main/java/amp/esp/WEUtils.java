package amp.esp;


import cmf.bus.Envelope;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class WEUtils {

    // ===================
    public static byte[] getBody(Envelope envelope) {
        return envelope.getPayload();
    }

    public static void setBody(Envelope envelope, byte[] bytes) {
        envelope.setPayload(bytes);
    }



    // ===================
    private static String getx(Envelope envelope, String key) {
        return envelope.getHeader(key);
    }

    private static void setx(Envelope envelope, String key, String value) {
        // TODO Auto-generated method stub
        envelope.setHeader(key, value);
    }

    // ===================
    public static String getEventType(Envelope envelope) {
        // TODO Auto-generated method stub
        return getx(envelope, "EventType");
    }

    public static void setEventType(Envelope envelope, String type) {
        // TODO Auto-generated method stub
        setx(envelope, "EventType", type);
    }

    // ===================
    public static String getReplyTo(Envelope envelope) {
        // TODO Auto-generated method stub
        return getx(envelope, "ReplyTo");
    }

    public static void setReplyTo(Envelope envelope, String replyTo) {
        // TODO Auto-generated method stub
        setx(envelope, "ReplyTo", replyTo);
    }

    // ===================
    public static String getTopic(Envelope envelope) {
        // TODO Auto-generated method stub
        return getx(envelope, "Topic");
    }

    public static void setTopic(Envelope envelope, String topic) {
        // TODO Auto-generated method stub
        setx(envelope, "Topic", topic);
    }

    // ===================
    public static String getId(Envelope envelope) {
        // TODO Auto-generated method stub
        return getx(envelope, "Id");
    }

    public static void setId(Envelope envelope, UUID symIdToRealId) {
        // TODO Auto-generated method stub
        setx(envelope, "Id", symIdToRealId.toString());

    }

    // ===================
    public static String getCorrelationId(Envelope envelope) {
        // TODO Auto-generated method stub
        return getx(envelope, "CorrelationId");
    }

    public static void setCorrelationId(Envelope envelope, UUID symIdToRealId) {
        // TODO Auto-generated method stub
        setx(envelope, "CorrelationId", symIdToRealId.toString());
    }


    // ===================
    // TODO check uses of timestamp (with goal toward removal)
    private Date timestamp;
    public static Date getTimestamp(Envelope envelope) {
        return new Date(Long.parseLong(getx(envelope, "Timestamp")));
    }

    public static void setTimestamp(Envelope envelope, Date timestamp) {
        // TODO Auto-generated method stub
        setx(envelope, "Timestamp", Long.toString(timestamp.getTime()));
    }

    // ===================
    public static Map<String, String> getHeaders(Envelope envelope) {
        return envelope.getHeaders();
    }

}
