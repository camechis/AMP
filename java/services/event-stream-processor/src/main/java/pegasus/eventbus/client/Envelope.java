package pegasus.eventbus.client;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class Envelope {

	cmf.bus.Envelope cmfenv = null;

	// Constructor for testing
	public Envelope() {
		super();
		cmfenv = new cmf.bus.Envelope();
	}

	public Envelope(cmf.bus.Envelope cmfenv) {
		super();
		this.cmfenv = cmfenv;
	}

	// ===================
	public byte[] getBody() {
		return cmfenv.getPayload();
	}

	public void setBody(byte[] bytes) {
		cmfenv.setPayload(bytes);
	}



	// ===================
	private String get(String key) {
		return cmfenv.getHeader(key);
	}

	private void set(String key, String value) {
		// TODO Auto-generated method stub
		cmfenv.setHeader(key, value);
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
		return cmfenv.getHeaders();
	}

}
