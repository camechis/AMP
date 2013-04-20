package amp.bus.rabbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.bus.IEnvelopeDispatcher;
import amp.bus.IEnvelopeReceivedCallback;
import amp.bus.ITransportProvider;
import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ITopologyService;
import amp.bus.rabbit.topology.ProducingRoute;
import amp.bus.rabbit.topology.RoutingInfo;
import cmf.bus.Envelope;
import cmf.bus.IRegistration;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

/**
 * AMQP Implementation of the Transport Provider using the RabbitMQ Client.
 * 
 * @author John Ruiz and Richard Clayton (Berico Technologies)
 */
public class RabbitTransportProvider implements ITransportProvider {

    protected IRabbitChannelFactory channelFactory;
    protected List<IEnvelopeReceivedCallback> envCallbacks = new ArrayList<IEnvelopeReceivedCallback>();
    protected ConcurrentHashMap<IRegistration, RabbitListener> listeners = new ConcurrentHashMap<IRegistration, RabbitListener>();
    protected Logger log;
    protected ITopologyService topologyService;
    
    /**
     * Initialize the Transport Provider with the Topology Service and the Channel Factory.
     * @param topologyService Service that determines the correct exchange and broker to send messages to.
     * @param channelFactory Service that uses topology information to establish connections to the broker.
     */
    public RabbitTransportProvider(ITopologyService topologyService, IRabbitChannelFactory channelFactory) {

		log = LoggerFactory.getLogger(this.getClass());
	
		this.topologyService = topologyService;
		this.channelFactory = channelFactory;
    }

    /**
     * Register a new Envelope handler for the specified routes.
     * @param registration Handlers and routing hints.
     */
    @Override
    public void register(IRegistration registration) throws Exception {
    		
        log.debug("Enter Register");

        // first, get the topology based on the registration info
        RoutingInfo routing = topologyService.getRoutingInfo(registration.getRegistrationInfo());

        for (ConsumingRoute route : routing.getConsumingRoutes()){
        	
        		RabbitListener listener = createListener(registration, route);
        		
        		// store the listener
                listeners.put(registration, listener);
        }

        log.debug("Leave Register");
    }

    protected RabbitListener createListener(
			IRegistration registration, ConsumingRoute route) throws Exception {
    	
    		// create a channel
        Channel channel = channelFactory.getChannelFor(route);

        // create a listener
        RabbitListener listener = this.getListener(registration, route);

        // hook into the listener's events
        listener.onEnvelopeReceived(new IEnvelopeReceivedCallback() {

            @Override
            public void handleReceive(IEnvelopeDispatcher dispatcher) {
            	
                raise_onEnvelopeReceivedEvent(dispatcher);
            }
        });
        
        listener.onClose(new IListenerCloseCallback() {

            @Override
            public void onClose(IRegistration registration) {
            	
                listeners.remove(registration);
            }
        });
        
        listener.onConnectionError(new ReconnectOnConnectionErrorCallback(channelFactory));

        listener.start(channel);
    		
        return listener;
    }
    
    /**
     * Get a new Rabbit Listener for the provided registration and exchange.
     * This was pulled out as an extension point for deriving classes, as well as,
     * to make testing a little easier.
     * 
     * @param registration Handlers and hints
     * @param route Routing Information
     * @return Listener that will pull messages from the broker and call the handlers
     * on the registration.
     */
    protected RabbitListener getListener(IRegistration registration, ConsumingRoute route){
    		
    		return new RabbitListener(registration, route);
    }
    
    @Override
    public void send(Envelope env) throws Exception {
    	
        log.debug("Enter Send");

        // first, get the topology based on the headers
        RoutingInfo routing = topologyService.getRoutingInfo(env.getHeaders());

        for (ProducingRoute route : routing.getProducingRoutes()){
        	
        		log.info("Sending to exchange: " + route.getExchange().getName());
        		
        		Channel channel = null;
        		
        		try {
        			
        			channel = channelFactory.getChannelFor(route);
        			
        			BasicProperties props = 
        				new BasicProperties.Builder()
        					.headers(
        						new HashMap<String, Object>(env.getHeaders()))
        					.build();
        			
        			if (route.getExchange().shouldDeclare()){
        				
        				channel.exchangeDeclare(
        					route.getExchange().getName(), route.getExchange().getExchangeType(), 
        					route.getExchange().isDurable(), route.getExchange().isAutoDelete(), 
        					route.getExchange().getArguments());
        			}
        			
        			for (String routingKey : route.getRoutingKeys()){
        				
        				channel.basicPublish(route.getExchange().getName(), routingKey, props, env.getPayload());
        			}
        			
        		} catch (Exception e) {
        			
             	log.error("Failed to send an envelope", e);
             	throw e;
             } 
        }
        
        log.debug("Leave Send");
    }

    /**
     * Unregister a registration with the bus (canceling the listener
     * and stopping consumption from the broker).
     * @param registration The original registration used to create
     * the listener.
     */
    @Override
    public void unregister(IRegistration registration) {
    	
    		RabbitListener listener = listeners.remove(registration);
    	
        if (listener != null) {
            
        		listener.stopListening();
        }
    }
    
    /**
     * Register a callback for the EnvelopeReceived event.
     * @param callback Called when an envelope is received.
     */
    @Override
    public void onEnvelopeReceived(IEnvelopeReceivedCallback callback) {
        envCallbacks.add(callback);
    }

    /**
     * Used to signal EnvelopeReceived listeners that a new envelope has been
     * received from a listener.
     * @param dispatcher Object used to grab the envelope as well as to complete
     * the process of receiving the message.
     */
    protected void raise_onEnvelopeReceivedEvent(IEnvelopeDispatcher dispatcher) {
        for (IEnvelopeReceivedCallback callback : envCallbacks) {
            try {
                callback.handleReceive(dispatcher);
            } catch (Exception ex) {
                log.error("Caught an unhandled exception raising the onEnvelopeReceived event", ex);
            }
        }
    }
    
    /**
     * Gracefully shutdown all the services associated with the Transport Provider.
     */
    @Override
    public void dispose() {
    		
        try {  channelFactory.dispose(); } catch (Exception ex) { }

        try {  topologyService.dispose(); } catch (Exception ex) { }

        for (RabbitListener l : listeners.values()) {
            
        		try { l.dispose(); } catch (Exception ex) { }
        }
    }

    /**
     * Called before the instance is recycled.
     */
    @Override
    protected void finalize() {
        dispose();
    }
}