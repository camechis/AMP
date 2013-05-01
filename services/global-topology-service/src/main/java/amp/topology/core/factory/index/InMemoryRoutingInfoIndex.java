package amp.topology.core.factory.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.topology.core.factory.index.IndexUtils.RoutingHints;
import amp.topology.core.model.Client;
import static amp.topology.core.factory.index.IndexUtils.*;

public class InMemoryRoutingInfoIndex implements RoutingInfoIndex {

	private static Logger logger = LoggerFactory.getLogger(InMemoryRoutingInfoIndex.class);
	
	private static String ALL = "*";
	
	ArrayList<RoutingInfoSelectionContext> index = new ArrayList<RoutingInfoSelectionContext>();
	
	public InMemoryRoutingInfoIndex(){}
	
	public InMemoryRoutingInfoIndex(Collection<RoutingInfoSelectionContext> selections){
		
		this.setRoutingInfoSelections(selections);
	}
	
	public void setRoutingInfoSelections(Collection<RoutingInfoSelectionContext> selections){
		
		if (selections != null){
			
			index.addAll(selections);
		}
	}
	
	@Override
	public List<RoutingInfoSelectionContext> getMatches(
			Client client, Map<String, String> routingContext) {
		
		RoutingHints hints = getHints(routingContext);
		
		ArrayList<RoutingInfoSelectionContext> selections = 
				new ArrayList<RoutingInfoSelectionContext>();
		
		for(RoutingInfoSelectionContext item : this.index){
			
			boolean isTopicMatch = isTopicMatch(item, hints.getTopic());
			boolean isPatternMatch = isPatternMatch(item, hints.getPattern());
			boolean isClientMatch = isClientMatch(item, client.getPrincipalName());
			boolean isGroupMatch = isGroupMatch(item, client.getAuthorities());
			
			if (isTopicMatch && isPatternMatch && isClientMatch && isGroupMatch){
				
				selections.add(item);
			}
		}
		
		return selections;
	}
	
	static boolean isTopicMatch(RoutingInfoSelectionContext item, String topic){
		
		boolean isTopicMatch = containsTopic(item.getIncludedTopics(), topic);
		
		if (!isTopicMatch){
			
			isTopicMatch = containsTopic(item.getIncludedTopics(), ALL)
					&& !containsTopic(item.getExcludedTopics(), topic);
		}
		
		return isTopicMatch;
	}
	
	static boolean isPatternMatch(RoutingInfoSelectionContext item, String pattern){
		
		if (item.getIncludedPatterns().size() == 0 && item.getExcludedPatterns().size() == 0){
			
			return true;
		}
		
		boolean isPatternMatch = containsPattern(item.getIncludedPatterns(), pattern);
		
		if (!isPatternMatch){
			
			isPatternMatch = containsPattern(item.getIncludedPatterns(), ALL)
					&& !containsPattern(item.getExcludedPatterns(), pattern);
		}
		
		return isPatternMatch;
	}
	
	
	static boolean isClientMatch(RoutingInfoSelectionContext item, String clientName){
		
		if (item.getIncludedClients().size() == 0 && item.getExcludedClients().size() == 0){
			
			return true;
		}
		
		boolean isClientMatch = containsItem(item.getIncludedClients(), clientName);
		
		if (!isClientMatch){
			
			isClientMatch = containsItem(item.getIncludedClients(), ALL)
					&& !containsItem(item.getExcludedClients(), clientName);
		}
		
		return isClientMatch;
	}
	
	static boolean isGroupMatch(RoutingInfoSelectionContext item, Collection<String> groups){
		
		if (item.getIncludedGroups().size() == 0 && item.getExcludedGroups().size() == 0){
			
			return true;
		}
		
		boolean isGroupMatch = containsItem(item.getIncludedGroups(), groups);
		
		if (!isGroupMatch){
			
			isGroupMatch = containsItem(item.getIncludedGroups(), ALL)
					&& !containsItem(item.getExcludedGroups(), groups);
		}
		
		return isGroupMatch;
	}
}
