package amp.topology.core.factory.index;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cmf.bus.EnvelopeHeaderConstants;

public class IndexUtils {
	
	public static boolean containsItem(
		Collection<String> targetCollection, Collection<String> testCollection){
		
		for (String test : testCollection){
			
			if (containsItem(targetCollection, test)){
				
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsItem(Collection<String> targetCollection, String target){
		
		for(String item : targetCollection){
			
			if (item.equals(target)){
				
				return true;
			}
		}
		
		return false;
	}
	
	public static RoutingHints getHints(Map<String, String> routingContext){
		
		return new RoutingHints(routingContext);
	}
	
	public static class RoutingHints extends HashMap<String, String> {
		
		private static final long serialVersionUID = 8484490892136914919L;
		
		public RoutingHints(Map<String, String> routingContext){
			
			if (routingContext != null){
				
				this.putAll(routingContext);
			}
		}
		
		public String getTopic(){
			
			return this.get(EnvelopeHeaderConstants.MESSAGE_TOPIC);
		}
		
		public String getPattern(){
			
			return this.get(EnvelopeHeaderConstants.MESSAGE_PATTERN);
		}
		
		public String getClient(){
			
			return this.get("__gts-client__");
		}
		
		public String getSenderIdentity(){
			
			return this.get(EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY);
		}
	}
}
