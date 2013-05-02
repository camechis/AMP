package amp.topology.core.factory.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.FactoryReference;

public class RoutingInfoSelectionContext {
	
	String id = UUID.randomUUID().toString();
	ArrayList<String> includedTopics = new ArrayList<String>();
	ArrayList<String> excludedTopics = new ArrayList<String>();
	ArrayList<String> includedClients = new ArrayList<String>();
	ArrayList<String> excludedClients = new ArrayList<String>();
	ArrayList<String> includedGroups = new ArrayList<String>();
	ArrayList<String> excludedGroups = new ArrayList<String>();
	ArrayList<String> includedPatterns = new ArrayList<String>();
	ArrayList<String> excludedPatterns = new ArrayList<String>();
	
	FactoryReference<RoutingInfo> routingInfo;
	
	public RoutingInfoSelectionContext(){}
	
	public RoutingInfoSelectionContext(
			Collection<String> includedTopics, 
			Collection<String> excludedTopics, 
			Collection<String> includedClients,
			Collection<String> excludedClients,
			Collection<String> includedGroups,
			Collection<String> excludedGroups,
			Collection<String> includedPatterns,
			Collection<String> excludedPatterns,
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
	
	public RoutingInfoSelectionContext(
			String id,
			Collection<String> includedTopics, 
			Collection<String> excludedTopics, 
			Collection<String> includedClients,
			Collection<String> excludedClients,
			Collection<String> includedGroups,
			Collection<String> excludedGroups,
			Collection<String> includedPatterns,
			Collection<String> excludedPatterns,
			FactoryReference<RoutingInfo> routingInfo) {
		
		this.id = id;
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<String> getIncludedTopics() {
		return includedTopics;
	}

	public void setIncludedTopics(Collection<String> includedTopics) {
		addAllIfNotNull(this.includedTopics, includedTopics);
	}

	public Collection<String> getExcludedTopics() {
		return excludedTopics;
	}

	public void setExcludedTopics(Collection<String> excludedTopics) {
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

	public Collection<String> getIncludedPatterns() {
		return includedPatterns;
	}

	public void setIncludedPatterns(Collection<String> includedPatterns) {
		addAllIfNotNull(this.includedPatterns, includedPatterns);
	}

	public Collection<String> getExcludedPatterns() {
		return excludedPatterns;
	}

	public void setExcludedPatterns(Collection<String> excludedPatterns) {
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
