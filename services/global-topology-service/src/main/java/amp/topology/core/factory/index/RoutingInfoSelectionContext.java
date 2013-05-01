package amp.topology.core.factory.index;

import java.util.ArrayList;
import java.util.Collection;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.MessagingPattern;
import amp.topology.core.model.Topic;

public class RoutingInfoSelectionContext {
	
	ArrayList<Topic> includedTopics = new ArrayList<Topic>();
	ArrayList<Topic> excludedTopics = new ArrayList<Topic>();
	ArrayList<String> includedClients = new ArrayList<String>();
	ArrayList<String> excludedClients = new ArrayList<String>();
	ArrayList<String> includedGroups = new ArrayList<String>();
	ArrayList<String> excludedGroups = new ArrayList<String>();
	ArrayList<MessagingPattern> includedPatterns = new ArrayList<MessagingPattern>();
	ArrayList<MessagingPattern> excludedPatterns = new ArrayList<MessagingPattern>();
	
	FactoryReference<RoutingInfo> routingInfo;
	
	public RoutingInfoSelectionContext(){}
	
	public RoutingInfoSelectionContext(
			Collection<Topic> includedTopics, 
			Collection<Topic> excludedTopics, 
			Collection<String> includedClients,
			Collection<String> excludedClients,
			Collection<String> includedGroups,
			Collection<String> excludedGroups,
			Collection<MessagingPattern> includedPatterns,
			Collection<MessagingPattern> excludedPatterns,
			FactoryReference<RoutingInfo> routingInfo) {
		
		setIncludedTopics(includedTopics);
		setExcludedTopics(excludedTopics);
		setIncludedClients(includedClients);
		setExcludedClients(excludedClients);
		setIncludedGroups(includedGroups);
		setExcludedGroups(excludedGroups);
		setIncludedPatterns(includedPatterns);
		setExcludedPatterns(excludedPatterns);
		
		this.routingInfo = routingInfo;
	}
	
	public Collection<Topic> getIncludedTopics() {
		return includedTopics;
	}

	public void setIncludedTopics(Collection<Topic> includedTopics) {
		addAllIfNotNull(this.includedTopics, includedTopics);
	}

	public Collection<Topic> getExcludedTopics() {
		return excludedTopics;
	}

	public void setExcludedTopics(Collection<Topic> excludedTopics) {
		addAllIfNotNull(this.excludedTopics, excludedTopics);
	}

	public Collection<String> getIncludedClients() {
		return includedClients;
	}

	public void setIncludedClients(Collection<String> includedClients) {
		addAllIfNotNull(this.includedClients, includedClients);
	}

	public Collection<String> getExcludedClients() {
		return excludedClients;
	}

	public void setExcludedClients(Collection<String> excludedClients) {
		addAllIfNotNull(this.excludedClients, excludedClients);
	}

	public Collection<String> getIncludedGroups() {
		return includedGroups;
	}

	public void setIncludedGroups(Collection<String> includedGroups) {
		addAllIfNotNull(this.includedGroups, includedGroups);
	}
	
	public Collection<String> getExcludedGroups() {
		return excludedGroups;
	}

	public void setExcludedGroups(Collection<String> excludedGroups) {
		addAllIfNotNull(this.excludedGroups, excludedGroups);
	}

	public Collection<MessagingPattern> getIncludedPatterns() {
		return includedPatterns;
	}

	public void setIncludedPatterns(Collection<MessagingPattern> includedPatterns) {
		addAllIfNotNull(this.includedPatterns, includedPatterns);
	}

	public Collection<MessagingPattern> getExcludedPatterns() {
		return excludedPatterns;
	}

	public void setExcludedPatterns(Collection<MessagingPattern> excludedPatterns) {
		addAllIfNotNull(this.excludedPatterns, excludedPatterns);
	}

	public FactoryReference<RoutingInfo> getRoutingInfo() {
		return routingInfo;
	}

	public void setRoutingInfo(FactoryReference<RoutingInfo> routingInfo) {
		this.routingInfo = routingInfo;
	}
	
	static <T> void addAllIfNotNull(ArrayList<T> target, Collection<T> additions){
		
		if (additions != null){
			
			target.addAll(additions);
		}
	}
}
