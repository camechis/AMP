package pegasus.eventbus.client;

import cmf.bus.Envelope;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class WrappedEnvelope {

	Envelope envelope = null;

	public Envelope getEnvelope() {
        return envelope;
    }

    // Constructor for testing
	public WrappedEnvelope() {
		super();
		envelope = new Envelope();
	}

	public WrappedEnvelope(Envelope cmfenv) {
		super();
		this.envelope = cmfenv;
	}

	// ===================
	public byte[] getBody() {
		return envelope.getPayload();
	}

	public void setBody(byte[] bytes) {
		envelope.setPayload(bytes);
	}



	// ===================
	private String get(String key) {
		return envelope.getHeader(key);
	}

	private void set(String key, String value) {
		// TODO Auto-generated method stub
		envelope.setHeader(key, value);
	}

	// ===================
	public String getEventType() {
		// TODO Auto-generated method stub
		return get("EventType");
	}

	public void setEventType(String type) {
		// TODO Auto-generated method stub
		set("EventType", type);
	}

	// ===================
	public String getReplyTo() {
		// TODO Auto-generated method stub
		return get("ReplyTo");
	}

	public void setReplyTo(String replyTo) {
		// TODO Auto-generated method stub
		set("ReplyTo", replyTo);
	}

	// ===================
	public String getTopic() {
		// TODO Auto-generated method stub
		return get("Topic");
	}

	public void setTopic(String topic) {
		// TODO Auto-generated method stub
		set("Topic", topic);
	}

	// ===================
	public String getId() {
		// TODO Auto-generated method stub
		return get("Id");
	}

	public void setId(UUID symIdToRealId) {
		// TODO Auto-generated method stub
		set("Id", symIdToRealId.toString());

	}

	// ===================
	public String getCorrelationId() {
		// TODO Auto-generated method stub
		return get("CorrelationId");
	}

	public void setCorrelationId(UUID symIdToRealId) {
		// TODO Auto-generated method stub
		set("CorrelationId", symIdToRealId.toString());
	}


	// ===================
	// TODO check uses of timestamp (with goal toward removal)
	private Date timestamp;
	public Date getTimestamp() {
		// TODO Auto-generated method stub
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		// TODO Auto-generated method stub
		this.timestamp = timestamp;
	}

	// ===================
	public Map<String, String> getHeaders() {
		return envelope.getHeaders();
	}

}
