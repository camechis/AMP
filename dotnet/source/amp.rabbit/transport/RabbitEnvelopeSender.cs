using System;
using System.Collections;
using System.Linq;
using amp.rabbit.connection;
using amp.rabbit.topology;
using cmf.bus;
using Common.Logging;
using RabbitMQ.Client;

namespace amp.rabbit.transport
{
    public class RabbitEnvelopeSender : IEnvelopeSender
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(RabbitEnvelopeSender));

        protected ITopologyService _topologyService;
        protected IRabbitConnectionFactory _connFactory;

        public RabbitEnvelopeSender(
            ITopologyService topologyService, 
            IRabbitConnectionFactory connFactory)
        {
            _topologyService = topologyService;
            _connFactory = connFactory;
        }

        public void Send(Envelope envelope)
        {
            Log.Debug("Enter Send");

            // first, get the topology based on the headers
            RoutingInfo routing = _topologyService.GetRoutingInfo(envelope.Headers);

            // for each exchange, send the envelope
            foreach (ProducingRoute route in routing.ProducingRoutes)
            {
                Exchange ex = route.Exchange;
                Log.Info("Sending to exchange: " + ex.ToString());

                //TODO: There is a problem here in how exceptions are being logged/rethrown:
                //It is possible for a envelope to go out on some exchanges and/or routes and then not others.
                //A failure aborts attempts at subequent exchanges/routes but does not role back previous.
                //Seems we should either continue to attempt later routes and throw at the end or role back.
                try {
                    IConnectionManager connMgr = _connFactory.ConnectTo(route.Brokers.First());

                    using (IModel channel = connMgr.CreateModel())
                    {
                        IBasicProperties props = channel.CreateBasicProperties();
                        props.Headers = envelope.Headers as IDictionary;

                        channel.ExchangeDeclare(ex.Name, ex.ExchangeType, ex.IsDurable, ex.IsAutoDelete, ex.Arguments);

                        foreach (string routingKey in route.RoutingKeys)
                        {
                            try
                            {
                                channel.BasicPublish(ex.Name, routingKey, props, envelope.Payload);

                            }
                            catch (Exception e)
                            {
                                Log.Error(string.Format("Failed to send an envelope to route: {0} on excange {1}.", routingKey, ex), e);
                                throw;
                            }
                        }

                        // close the channel, but not the connection.  Channels are cheap.
                        channel.Close();
                    }

                } catch (Exception e) {
                    Log.Error("Failed to send an envelope", e);
                    throw;
                }
            }

            Log.Debug("Leave Send");        
        }

        public void Dispose()
        {
            _topologyService.Dispose();
            _connFactory.Dispose();
        }
    }
}
