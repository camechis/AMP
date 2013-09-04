using System.Collections.Generic;
using System.Threading;
using Common.Logging;

using amp.rabbit;
using amp.rabbit.topology;
using cmf.bus;

namespace amp.bus.rabbit
{
    public class RabbitEnvelopeReceiver : IEnvelopeReceiver
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(RabbitTransportProvider));

        protected IDictionary<IRegistration, RabbitListener> _listeners;
        protected ITopologyService _topologyService;
        protected IRabbitConnectionFactory _connFactory;

        public RabbitEnvelopeReceiver(
            ITopologyService topologyService, 
            IRabbitConnectionFactory connFactory)
        {
            _topologyService = topologyService;
            _connFactory = connFactory;

            _listeners = new Dictionary<IRegistration, RabbitListener>();
        }

        public void Register(IRegistration registration)
        {
            Log.Debug("Enter Register");

            // first, get the topology based on the registration info
            RoutingInfo routing = _topologyService.GetRoutingInfo(registration.Info);

            // next, pull out all the producer exchanges
            List<Exchange> exchanges = new List<Exchange>();

            foreach (RouteInfo route in routing.Routes) 
            {

                exchanges.Add(route.ConsumerExchange);
            }

            foreach (var exchange in exchanges)
            {
                RabbitListener listener = createListener(registration, exchange);

                // store the listener
                _listeners.Add(registration, listener);
            }

            Log.Debug("Leave Register");        
        }

        public void Unregister(IRegistration registration)
        {
            RabbitListener listener;

            if (_listeners.TryGetValue(registration, out listener))
            {
                listener.Stop();
            }
        }

        //TODO: Address fact that IEnvelopeReceiver does not implement IDisposable
        public void Dispose()
        {

            try {  _connFactory.Dispose(); } catch { }

            //TODO: Resolve that toposervice does not implement IDisposable
            //try {  _topologyService.Dispose; } catch { }

            foreach (RabbitListener listener in _listeners.Values) 
            {
                //TODO: Resolve that RabbitListener does not implement IDisposable
                //try { listener.Dispose(); } catch { }
            }
        }

        protected RabbitListener createListener(IRegistration registration, Exchange exchange) 
        {
            // create a channel
            var connection = _connFactory.ConnectTo(exchange);

            // create a listener
            RabbitListener listener = new RabbitListener(registration, exchange, connection);
            listener.OnEnvelopeReceived += dispatcher =>
                {
                    Log.Debug("Got an envelope from the RabbitListener: dispatching.");
                    // the dispatcher encapsulates the logic of giving the envelope to handlers
                    dispatcher.Dispatch();
                };
            listener.OnClose += _listeners.Remove;

            //TODO: Resolve that RabbitListener does not implement OnConnectionError
            //listener.OnConnectionError(new ReconnectOnConnectionErrorCallback(_channelFactory));

            // put it on another thread so as not to block this one
            Thread listenerThread = new Thread(listener.Start);
            listenerThread.Name = string.Format("{0} on {1}:{2}{3}", exchange.QueueName, exchange.HostName, exchange.Port, exchange.VirtualHost);
            listenerThread.Start();

            return listener;
        }
    }
}
