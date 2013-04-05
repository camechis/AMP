package amp.extensions.commons.builder;

import java.util.LinkedList;

import amp.bus.DefaultEnvelopeBus;
import amp.bus.IEnvelopeProcessor;

import cmf.bus.IEnvelopeBus;

/**
 * Fluent for constructing an EnvelopeBus.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class EnvelopeBusBuilder extends FluentExtension {

	LinkedList<IEnvelopeProcessor> inbound = new LinkedList<IEnvelopeProcessor>();
	LinkedList<IEnvelopeProcessor> outbound = new LinkedList<IEnvelopeProcessor>();
	
	/**
	 * Instantiate the Builder with reference to the parent fluent.
	 * @param parent BusBuilder fluent.
	 */
	public EnvelopeBusBuilder(BusBuilder parent) {
		
		super(parent);
	}
	
	/**
	 * Add an inbound processor.
	 * @param processor Processor to add.
	 * @return This.
	 */
	public EnvelopeBusBuilder addInbound(IEnvelopeProcessor processor){
		
		this.inbound.add(processor);
		
		return this;
	}
	
	/**
	 * Add an outbound processor.
	 * @param processor Processor to add.
	 * @return This.
	 */
	public EnvelopeBusBuilder addOutbound(IEnvelopeProcessor processor){
		
		this.outbound.add(processor);
		
		return this;
	}
	
	/**
	 * Get an instance of the Envelope Bus.
	 * @return An instance of an Envelope Bus.
	 */
	public IEnvelopeBus getInstance(){
		
		return new DefaultEnvelopeBus(
			this.parent.configure().transportProvider, inbound, outbound);
	}

}
