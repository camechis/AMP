﻿using System.Collections.Generic;
using System.Linq;
using System.Threading;
using amp.rabbit.connection;
using amp.rabbit.dispatch;
using amp.rabbit.topology;
using cmf.bus;
using Common.Logging;

namespace amp.rabbit.transport
{
    public class RabbitEnvelopeReceiver : IEnvelopeReceiver
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(RabbitEnvelopeReceiver));

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

            foreach (var route in routing.ConsumingRoutes) 
            {
                exchanges.Add(route.Exchange);
                RabbitListener listener = createListener(registration, route);
            }

            Log.Debug("Leave Register");        
        }

        public void Unregister(IRegistration registration)
        {
            RabbitListener listener;

            if (_listeners.TryGetValue(registration, out listener))
            {
                try { listener.Stop(); } catch { }
            }
        }

        public void Dispose()
        {
            foreach (RabbitListener listener in _listeners.Values) 
            {
                try { listener.Stop(); } catch { }
            }

            _topologyService.Dispose();
            _connFactory.Dispose();
        }

        protected RabbitListener createListener(IRegistration registration, ConsumingRoute route) 
        {
            IConnectionManager connection = _connFactory.ConnectTo(route.Brokers.First());

            // create a listener
            RabbitListener listener = new RabbitListener(registration, route, connection);
            listener.OnEnvelopeReceived += dispatcher =>
                {
                    Log.Debug("Got an envelope from the RabbitListener: dispatching.");
                    // the dispatcher encapsulates the logic of giving the envelope to handlers
                    dispatcher.Dispatch();
                };
            listener.OnClose += _listeners.Remove;

            //TODO: Resolve that RabbitListener does not implement OnConnectionError
            //listener.OnConnectionError(new ReconnectOnConnectionErrorCallback(_channelFactory));

            // store the listener
            _listeners.Add(registration, listener);

            listener.Start();

            return listener;
        }
    }
}
