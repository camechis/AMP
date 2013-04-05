package amp.extensions.commons.builder;

/**
 * Fluent Bus-Build'n API.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class BusBuilder {
	
	/**
	 * 'New' a builder up.
	 */
	public BusBuilder(){}
	
	/**
	 * Placebo method to make you feel better about the
	 * construction process. In reality, you just ask
	 * eventBus() or envelopeBus() for what you want.
	 * 
	 * This is a good stopping point if you need to 
	 * grab both an instance of the EnvelopeBus and
	 * Event Bus.
	 * 
	 * @return This.
	 */
	public BusBuilder done(){
		
		return this;
	}
	
	/**
	 * Configure the transport provider.
	 * @return Transport Builder interface.
	 */
	public TransportBuilder configure(){
		
		return new TransportBuilder(this);
	}
	
	/**
	 * Configure and Get the Envelope Bus.
	 * @return EnvelopeBus Fluent.
	 */
	public EnvelopeBusBuilder envelopeBus(){
		
		return new EnvelopeBusBuilder(this);
	}
	
	/**
	 * Configure and Get the Event Bus.
	 * @return EventBus Fluent.
	 */
	public EventBusBuilder eventBus(){
		
		return new EventBusBuilder(this);
	}
	
	/**
	 * Simplifies configuration by using the default settings
	 * for a bus hosted on your local machine with the 
	 * default ports, users/passwords, etc.
	 * @return Fluent for retrieving Bus instances with the
	 * default configuration.
	 */
	public static LocalInstanceBuilder localProfile(){
		
		return new LocalInstanceBuilder();	
	}
}