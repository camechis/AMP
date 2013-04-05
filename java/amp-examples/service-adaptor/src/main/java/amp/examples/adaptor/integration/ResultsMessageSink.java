package amp.examples.adaptor.integration;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.IEnvelopeBus;

public class ResultsMessageSink {

	private static final Logger logger = LoggerFactory.getLogger(ResultsMessageSink.class);
	
	ArrayList<String> resultsToWatch = new ArrayList<String>();
	
	IEnvelopeBus envelopeBus;
	
	ResultsQueue resultsQueue;
	
	public ResultsMessageSink(IEnvelopeBus envelopeBus, ResultsQueue resultsQueue, List<String> resultsToWatch){
		
		this.envelopeBus = envelopeBus;
		this.resultsQueue = resultsQueue;
		this.resultsToWatch.addAll(resultsToWatch);
	}
	
	public void initialize() throws Exception {
		
		for (String resultToWatch : this.resultsToWatch){
			
			logger.info("Registering for Result Event: {}", resultToWatch);
			
			this.envelopeBus.register(new ResultsQueuingEnvelopeHandler(resultsQueue, resultToWatch));
		}
	}
	
}
