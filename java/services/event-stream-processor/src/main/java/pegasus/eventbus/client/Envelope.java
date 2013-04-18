package pegasus.eventbus.client;

import java.util.Date;
import java.util.UUID;


public class Envelope extends cmf.bus.Envelope {

	cmf.bus.Envelope cmfenv = null;

	public byte[] getBody() {
		return this.getPayload();
	}

	public String getEventType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReplyTo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTopic() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCorrelationId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEventType(String type) {
		// TODO Auto-generated method stub

	}

	public void setId(UUID symIdToRealId) {
		// TODO Auto-generated method stub

	}

	public void setCorrelationId(UUID symIdToRealId) {
		// TODO Auto-generated method stub

	}

	public void setTopic(String topic) {
		// TODO Auto-generated method stub

	}

	public void setReplyTo(String replyTo) {
		// TODO Auto-generated method stub

	}

	public void setTimestamp(Date timestamp) {
		// TODO Auto-generated method stub

	}

	public void setBody(byte[] bytes) {
		// TODO Auto-generated method stub

	}

}
