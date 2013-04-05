package amp.examples.adaptor.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import amp.examples.adaptor.integration.ResultsQueue;

import cmf.bus.Envelope;

import com.yammer.dropwizard.views.View;

public class ResultQueueView extends View {

	UserDetails userDetails;
	ResultsQueue results;
	
	public ResultQueueView(ResultsQueue results, UserDetails userDetails) {
		
		super("results.ftl");
		
		this.results = results;
	}

	public Collection<FriendlyEnvelope> getResults(){
		
		List<FriendlyEnvelope> envelopes = new ArrayList<FriendlyEnvelope>();
		
		for (Envelope envelope : this.results.get()){
			
			envelopes.add(new FriendlyEnvelope(envelope));
		}
		
		return envelopes;
	}

	public UserDetails getUserDetails() {
		
		return userDetails;
	}
}
