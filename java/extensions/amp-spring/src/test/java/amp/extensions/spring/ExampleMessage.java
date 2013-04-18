package amp.extensions.spring;

public class ExampleMessage {

	String sender;
	String message;
	long timestamp;
	
	public ExampleMessage(){}
	
	public ExampleMessage(String sender, String message, long timestamp) {
		super();
		this.sender = sender;
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ExampleMessage [sender=" + sender + ", message=" + message
				+ ", timestamp=" + timestamp + "]";
	}
}