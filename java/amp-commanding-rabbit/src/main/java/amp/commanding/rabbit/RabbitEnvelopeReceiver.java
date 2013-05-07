package amp.commanding.rabbit;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import amp.bus.IEnvelopeDispatcher;
import amp.bus.IEnvelopeReceivedCallback;
import amp.rabbit.IListenerCloseCallback;
import amp.rabbit.IRabbitChannelFactory;
import amp.rabbit.ReconnectOnConnectionErrorCallback;
import cmf.bus.IEnvelopeReceiver;
import cmf.bus.IRegistration;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.rabbit.RabbitListener;
import amp.rabbit.topology.Exchange;
import amp.rabbit.topology.ITopologyService;
import amp.rabbit.topology.RouteInfo;
import amp.rabbit.topology.RoutingInfo;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class RabbitEnvelopeReceiver implements IEnvelopeReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitEnvelopeReceiver.class);

    private ITopologyService _topologyService;
    private IRabbitChannelFactory _channelFactory;
    private ConcurrentHashMap<IRegistration, RabbitListener> _listeners;
    private List<IEnvelopeReceivedCallback> _envCallbacks;



    public RabbitEnvelopeReceiver(ITopologyService topologyService, IRabbitChannelFactory channelFactory) {

        _topologyService = topologyService;
        _channelFactory = channelFactory;

        _listeners = new ConcurrentHashMap<IRegistration, RabbitListener>();
        _envCallbacks = new ArrayList<IEnvelopeReceivedCallback>();
    }



    @Override
    public void register(IRegistration registration) throws Exception {

        LOG.debug("Enter Register");

        // first, get the topology based on the registration info
        RoutingInfo routing = _topologyService.getRoutingInfo(registration.getRegistrationInfo());

        // next, pull out all the producer exchanges
        List<Exchange> exchanges = new ArrayList<Exchange>();

        for (RouteInfo route : routing.getRoutes()) {

            exchanges.add(route.getConsumerExchange());
        }

        for (Exchange exchange : exchanges) {

            RabbitListener listener = createListener(registration, exchange);

            // store the listener
            _listeners.put(registration, listener);
        }

        LOG.debug("Leave Register");
    }

    @Override
    public void unregister(IRegistration registration) throws Exception {
        RabbitListener listener = _listeners.remove(registration);

        if (listener != null) {

            listener.stopListening();
        }
    }

    @Override
    public void dispose() {

        try {  _channelFactory.dispose(); } catch (Exception ex) { }

        try {  _topologyService.dispose(); } catch (Exception ex) { }

        for (RabbitListener l : _listeners.values()) {

            try { l.dispose(); } catch (Exception ex) { }
        }
    }



    protected RabbitListener createListener(
            IRegistration registration, Exchange exchange) throws Exception {

        // create a channel
        Channel channel = _channelFactory.getChannelFor(exchange);

        // create a listener
        RabbitListener listener = this.getListener(registration, exchange);

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

                _listeners.remove(registration);
            }
        });

        listener.onConnectionError(new ReconnectOnConnectionErrorCallback(_channelFactory));

        listener.start(channel);

        return listener;
    }

    /**
     * Get a new Rabbit Listener for the provided registration and exchange.
     * This was pulled out as an extension point for deriving classes, as well as,
     * to make testing a little easier.
     *
     * @param registration Handlers and hints
     * @param exchange Routing Information
     * @return Listener that will pull messages from the broker and call the handlers
     * on the registration.
     */
    protected RabbitListener getListener(IRegistration registration, Exchange exchange) {

        return new RabbitListener(registration, exchange);
    }


    /**
     * Used to signal EnvelopeReceived listeners that a new envelope has been
     * received from a listener.
     * @param dispatcher Object used to grab the envelope as well as to complete
     * the process of receiving the message.
     */
    protected void raise_onEnvelopeReceivedEvent(IEnvelopeDispatcher dispatcher) {
        for (IEnvelopeReceivedCallback callback : _envCallbacks) {
            try {
                callback.handleReceive(dispatcher);
            } catch (Exception ex) {
                LOG.error("Caught an unhandled exception raising the onEnvelopeReceived event", ex);
            }
        }
    }

    /**
     * Called before the instance is recycled.
     */
    @Override
    protected void finalize() {
        this.dispose();
    }
}
