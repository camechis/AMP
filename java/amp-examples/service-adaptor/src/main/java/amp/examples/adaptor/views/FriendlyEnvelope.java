package amp.examples.adaptor.views;

import java.util.Map.Entry;

import cmf.bus.Envelope;

public class FriendlyEnvelope {

	Envelope envelope;
	
	public FriendlyEnvelope(Envelope envelope){
		
		this.envelope = envelope;
	}
	
	public String getFormattedHeaders(){
		
		StringBuilder sb = new StringBuilder();
		
		for (Entry<String, String> header : envelope.getHeaders().entrySet()){
			
			sb.append(header.getKey())
			  .append(": ")
			  .append(header.getValue())
			  .append("<br />");
		}
		
		return sb.toString();
	}
	
	public String getStringPayload(){
		
		return new String(envelope.getPayload());
	}
}
