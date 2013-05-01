package amp.topology.core.factory.index;

import static org.junit.Assert.*;
import static amp.topology.test.Utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import amp.bus.rabbit.topology.RoutingInfo;
import amp.topology.core.factory.FactoryReference;
import amp.topology.core.model.Client;
import amp.topology.core.model.MessagingPattern;
import amp.topology.core.model.Topic;

public class InMemoryRoutingInfoIndexTest {
	
	
	@Test
	public void isTopicMatch_returns_true_if_topic_is_in_included_bucket() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedTopics(
			Arrays.asList(
				topic("test.Topic"),
				topic("blah.test.Topic")));
		
		assertTrue(InMemoryRoutingInfoIndex.isTopicMatch(context, "test.Topic"));
		assertFalse(InMemoryRoutingInfoIndex.isTopicMatch(context, "no.a.match"));
	}
	
	@Test
	public void isTopicMatch_returns_true_if_context_allows_all_topics() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedTopics(
			Arrays.asList(
				topic("*")));
		
		assertTrue(InMemoryRoutingInfoIndex.isTopicMatch(context, "test.Topic"));
	}
	
	@Test
	public void isTopicMatch_returns_false_if_context_allows_all_topics_but_supplied_topic_is_excluded() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedTopics(
			Arrays.asList(
				topic("*")));
		
		context.setExcludedTopics(
			Arrays.asList(
				topic("test.Topic")));
		
		assertFalse(InMemoryRoutingInfoIndex.isTopicMatch(context, "test.Topic"));
		assertTrue(InMemoryRoutingInfoIndex.isTopicMatch(context, "test.other.Topic"));
	}
	
	@Test
	public void isPatternMatch_returns_true_if_pattern_is_in_included_bucket() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedPatterns(
			Arrays.asList(
				pattern("wiretap"),
				pattern("rpc")));
		
		assertTrue(InMemoryRoutingInfoIndex.isPatternMatch(context, "rpc"));
		assertFalse(InMemoryRoutingInfoIndex.isPatternMatch(context, "pubsub"));
	}
	
	@Test
	public void isPatternMatch_returns_true_if_context_allows_all_patterns() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedPatterns(
				Arrays.asList(
					pattern("*")));
			
		assertTrue(InMemoryRoutingInfoIndex.isPatternMatch(context, "pubsub"));
	}
	
	@Test
	public void isPatternMatch_returns_false_if_context_allows_all_patterns_but_supplied_pattern_is_excluded() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedPatterns(
				Arrays.asList(
					pattern("*")));
		
		context.setExcludedPatterns(
				Arrays.asList(
					pattern("wiretap")));
		
		assertFalse(InMemoryRoutingInfoIndex.isPatternMatch(context, "wiretap"));
		assertTrue(InMemoryRoutingInfoIndex.isPatternMatch(context, "pubsub"));
	}
	
	@Test
	public void isClientMatch_returns_true_if_client_is_in_included_bucket() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedClients(
			Arrays.asList("john", "rich"));
		
		assertTrue(InMemoryRoutingInfoIndex.isClientMatch(context, "rich"));
		assertFalse(InMemoryRoutingInfoIndex.isClientMatch(context, "travis"));
	}
	
	@Test
	public void isClientMatch_returns_true_if_context_allows_all_clients() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedClients(
				Arrays.asList("*"));
			
		assertTrue(InMemoryRoutingInfoIndex.isClientMatch(context, "rich"));
	}
	
	@Test
	public void isClientMatch_returns_false_if_context_allows_all_clients_but_supplied_client_is_excluded() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedClients(
				Arrays.asList("*"));
		
		context.setExcludedClients(
				Arrays.asList("rich"));
		
		assertFalse(InMemoryRoutingInfoIndex.isClientMatch(context, "rich"));
		assertTrue(InMemoryRoutingInfoIndex.isClientMatch(context, "john"));
	}
	
	@Test
	public void isGroupMatch_returns_true_if_group_is_in_included_bucket() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedGroups(
			Arrays.asList("super users", "admins"));
		
		assertTrue(InMemoryRoutingInfoIndex.isGroupMatch(
				context, Arrays.asList("admins", "testers")));
		assertFalse(InMemoryRoutingInfoIndex.isGroupMatch(
				context, Arrays.asList("users", "helpers")));
	}
	
	@Test
	public void isGroupMatch_returns_true_if_context_allows_all_groups() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedGroups(
				Arrays.asList("*"));
			
		assertTrue(InMemoryRoutingInfoIndex.isGroupMatch(
				context, Arrays.asList("users")));
	}
	
	@Test
	public void isGroupMatch_returns_false_if_context_allows_all_groups_but_supplied_group_is_excluded() {
		
		RoutingInfoSelectionContext context = new RoutingInfoSelectionContext();
		
		context.setIncludedGroups(
				Arrays.asList("*"));
		
		context.setExcludedGroups(
				Arrays.asList("users"));
		
		assertFalse(InMemoryRoutingInfoIndex.isGroupMatch(
				context, Arrays.asList("users")));
		assertTrue(InMemoryRoutingInfoIndex.isGroupMatch(
				context, Arrays.asList("admins")));
	}
	
	@Test
	public void match_test_1__explicit_topic_and_pattern_implicit_user_and_no_group(){
		
		InMemoryRoutingInfoIndex index = 
			new InMemoryRoutingInfoIndex(getSelections());
		
		Map<String, String> context = getRoutingContext("test.Topic", "pubsub");
		
		Client client = getClient("john", "users", "admins");
		
		List<RoutingInfoSelectionContext> selections = 
				index.getMatches(client, context);
	
		assertEquals(1, selections.size());
		assertEquals("test.Topic#no-wiretaps", selections.get(0).getRoutingInfo().getContext());
	}
	
	@Test
	public void match_test_2__explicit_topic_pattern_and_user_and_no_group(){
		
		InMemoryRoutingInfoIndex index = 
			new InMemoryRoutingInfoIndex(getSelections());
		
		Map<String, String> context = getRoutingContext("test.Topic", "wiretap");
		
		Client client = getClient("wiretapper", "users", "admins");
		
		List<RoutingInfoSelectionContext> selections = 
				index.getMatches(client, context);
	
		assertEquals(1, selections.size());
		assertEquals("test.Topic#wiretap", selections.get(0).getRoutingInfo().getContext());
	}
	
	@Test
	public void match_test_3__explicit_topic_implicit_pattern_and_user_and_exclusionary_group(){
		
		InMemoryRoutingInfoIndex index = 
			new InMemoryRoutingInfoIndex(getSelections());
		
		Map<String, String> context = getRoutingContext("blah.Blah", "wiretap");
		
		Client adminClient = getClient("bill", "users", "admins");
		
		List<RoutingInfoSelectionContext> adminRoute = 
				index.getMatches(adminClient, context);
		
		assertEquals("blah.Blah#admins", 
				adminRoute.get(0).getRoutingInfo().getContext());
		
		Client nonAdminClient = getClient("bob", "users");
		
		List<RoutingInfoSelectionContext> nonAdminRoute = 
				index.getMatches(nonAdminClient, context);
	
		assertEquals("blah.Blah#not-admins", 
			nonAdminRoute.get(0).getRoutingInfo().getContext());
	}
	
	protected Collection<RoutingInfoSelectionContext> getSelections(){
		
		ArrayList<RoutingInfoSelectionContext> selections = 
				new ArrayList<RoutingInfoSelectionContext>();
		
		RoutingInfoSelectionContext context1 = 
			generateContext(
				new String[]{ "test.Topic", "js.test.Topic" },
				new String[]{ },
				new String[]{ "pubsub", "rpc" },
				new String[]{ "wiretap" },
				new String[]{ "*" },
				new String[]{ "wiretapper" },
				new String[]{ },
				new String[]{ },
				"definition-factory", "test.Topic#no-wiretaps");
		
		selections.add(context1);
		
		RoutingInfoSelectionContext context2 = 
				generateContext(
					new String[]{ "test.Topic", "js.test.Topic" },
					new String[]{ },
					new String[]{ "wiretap" },
					new String[]{ },
					new String[]{ "wiretapper" },
					new String[]{ },
					new String[]{ },
					new String[]{ },
					"definition-factory", "test.Topic#wiretap");
			
		selections.add(context2);
		
		RoutingInfoSelectionContext context3 = 
				generateContext(
					new String[]{ "blah.Blah" },
					new String[]{ "blah.NotBlah" },
					new String[]{ "*" },
					new String[]{ },
					new String[]{ "*" },
					new String[]{ },
					new String[]{ "*" },
					new String[]{ "admins" },
					"definition-factory", "blah.Blah#not-admins");
			
		selections.add(context3);
		
		RoutingInfoSelectionContext context4 = 
				generateContext(
					new String[]{ "blah.Blah" },
					new String[]{ },
					new String[]{ "*" },
					new String[]{ },
					new String[]{ "*" },
					new String[]{ },
					new String[]{ "admins" },
					new String[]{ },
					"definition-factory", "blah.Blah#admins");
			
		selections.add(context4);
		
		return selections;
	}
	
	protected RoutingInfoSelectionContext generateContext(
		String[] iTopics, String[] xTopics, 
		String[] iPatterns, String[] xPatterns,
		String[] iClients, String[] xClients, 
		String[] iGroups, String[] xGroups,
		String factory, String context){
	
		RoutingInfoSelectionContext risc = 
				new RoutingInfoSelectionContext();
		
		ArrayList<Topic> inTopics = new ArrayList<Topic>();
		
		for(String itopic : iTopics){
			
			inTopics.add(topic(itopic));
		}
		
		ArrayList<Topic> exTopics = new ArrayList<Topic>();
		
		for(String xtopic : xTopics){
			
			exTopics.add(topic(xtopic));
		}
		
		ArrayList<MessagingPattern> inPatterns = new ArrayList<MessagingPattern>();
		
		for(String ipattern : iPatterns){
			
			inPatterns.add(pattern(ipattern));
		}
		
		ArrayList<MessagingPattern> exPatterns = new ArrayList<MessagingPattern>();
		
		for(String xpattern : xPatterns){
			
			exPatterns.add(pattern(xpattern));
		}
		
		risc.setIncludedTopics(inTopics);
		risc.setExcludedTopics(exTopics);
		risc.setIncludedPatterns(inPatterns);
		risc.setExcludedPatterns(exPatterns);
		risc.setIncludedClients(Arrays.asList(iClients));
		risc.setExcludedClients(Arrays.asList(xClients));
		risc.setIncludedGroups(Arrays.asList(iGroups));
		risc.setExcludedGroups(Arrays.asList(xGroups));
		
		risc.setRoutingInfo(new FactoryReference<RoutingInfo>(factory, context));
		
		return risc;
	}
}
