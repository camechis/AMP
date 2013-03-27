package amp.examples.adaptor.integration;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

import cmf.bus.Envelope;

public class ResultsQueue {

	public ConcurrentLinkedQueue<Envelope> queue = new ConcurrentLinkedQueue<Envelope>();
	
	public ResultsQueue(){}
	
	public void add(Envelope envelope){
		
		queue.add(envelope);
	}
	
	public void clear(){
		
		queue.clear();
	}
	
	public Collection<Envelope> get(){
		
		return Collections.unmodifiableCollection(queue);
	}
}
