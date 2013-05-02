package amp.topology.core.factory.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
		
		logger.debug("Routing Hints: {}", hints);
		
		ArrayList<RoutingInfoSelectionContext> selections = 
				new ArrayList<RoutingInfoSelectionContext>();
		
		for(RoutingInfoSelectionContext item : this.index){
			
			boolean isTopicMatch = isTopicMatch(item, hints.getTopic());
			boolean isPatternMatch = isPatternMatch(item, hints.getPattern());
			boolean isClientMatch = isClientMatch(item, client.getPrincipalName());
			boolean isGroupMatch = isGroupMatch(item, client.getAuthorities());
			
			logger.debug("{}=>{}: topic={}, pattern={}, client={}, group={}", 
				new Object[]{
					item.getRoutingInfo().getFactoryName(),
					item.getRoutingInfo().getContext(),
					isTopicMatch, isPatternMatch, isClientMatch, isGroupMatch });
			
			if (isTopicMatch && isPatternMatch && isClientMatch && isGroupMatch){
				
				selections.add(item);
			}
		}
		
		return selections;
	}
	
	static boolean isTopicMatch(RoutingInfoSelectionContext item, String topic){
		
		boolean isTopicMatch = containsItem(item.getIncludedTopics(), topic);
		
		if (!isTopicMatch){
			
			isTopicMatch = containsItem(item.getIncludedTopics(), ALL)
					&& !containsItem(item.getExcludedTopics(), topic);
		}
		
		return isTopicMatch;
	}
	
	static boolean isPatternMatch(RoutingInfoSelectionContext item, String pattern){
		
		if (item.getIncludedPatterns().size() == 0 && item.getExcludedPatterns().size() == 0){
			
			return true;
		}
		
		boolean isPatternMatch = containsItem(item.getIncludedPatterns(), pattern);
		
		if (!isPatternMatch){
			
			isPatternMatch = containsItem(item.getIncludedPatterns(), ALL)
					&& !containsItem(item.getExcludedPatterns(), pattern);
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

	@Override
	public boolean create(RoutingInfoSelectionContext context) {
		
		if (context != null){
		
			this.index.add(context);
			
			return true;
		}
		return false;
	}

	@Override
	public boolean update(RoutingInfoSelectionContext context) {
		
		boolean wasUpdated = false;
		
		RoutingInfoSelectionContext target = null;
		
		for (RoutingInfoSelectionContext i : index){
			
			if(i.getId().equals(context.getId())){
				target = i;
			}
		}
		
		if (target != null){
			
			index.remove(target);
			
			wasUpdated = true;
		}
		
		index.add(context);
		
		return wasUpdated;
	}

	@Override
	public boolean delete(String id) {
		
		RoutingInfoSelectionContext target = null;
		
		for (RoutingInfoSelectionContext i : index){
			
			if(i.getId().equals(id)){
				target = i;
			}
		}
		
		if (target != null){
			
			return index.remove(target);
		}
		
		return false;
	}

	@Override
	public RoutingInfoSelectionContext get(String id) {
		
		for (RoutingInfoSelectionContext i : index){
			
			if(i.getId().equals(id)){
				
				return i;
			}
		}
		return null;
	}

	@Override
	public List<RoutingInfoSelectionContext> all() {
		
		return index;
	}

	@Override
	public Collection<String> topics(String filter) {
		
		TreeSet<String> topics = new TreeSet<String>();
		
		for(RoutingInfoSelectionContext context : this.index){
			
			topics.addAll(context.getIncludedTopics());
			topics.addAll(context.getExcludedTopics());
		}
		
		return topics;
	}

	@Override
	public Collection<String> messagePatterns(String filter) {
		
		TreeSet<String> patterns = new TreeSet<String>();
		
		for(RoutingInfoSelectionContext context : this.index){
			
			patterns.addAll(context.getIncludedPatterns());
			patterns.addAll(context.getExcludedPatterns());
		}
		
		return patterns;
	}

	@Override
	public Collection<String> clients(String filter) {
		
		TreeSet<String> clients = new TreeSet<String>();
		
		for(RoutingInfoSelectionContext context : this.index){
			
			clients.addAll(context.getIncludedClients());
			clients.addAll(context.getExcludedClients());
		}
		
		return clients;
	}

	@Override
	public Collection<String> groups(String filter) {
		
		TreeSet<String> groups = new TreeSet<String>();
		
		for(RoutingInfoSelectionContext context : this.index){
			
			groups.addAll(context.getIncludedGroups());
			groups.addAll(context.getExcludedGroups());
		}
		
		return groups;
	}
}
