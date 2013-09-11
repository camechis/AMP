using System;
using System.Collections;
using System.Collections.Generic;
using amp.messaging;
using cmf.bus;
using cmf.eventing.patterns.rpc;
using Common.Logging;

namespace amp.eventing
{
    public class DefaultRpcBus : DefaultEventBus, IRpcEventBus
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(DefaultRpcBus));

        private readonly IEnvelopeBus _envelopeBus;
        
        public DefaultRpcBus(IEnvelopeBus envelopeBus
            , List<IMessageProcessor> inboundChain
            , List<IMessageProcessor> outboundChain)
            : base(envelopeBus, inboundChain, outboundChain)
        {
            _envelopeBus = envelopeBus;
        }


        public object GetResponseTo(object request, TimeSpan timeout, string expectedTopic)
        {
            Log.Debug("Enter GetResponseTo");

            if (null == request) { throw new ArgumentNullException("Cannot get response to a null request"); }

            // the response we're going to get
            object response = null;

            try
            {
                // create the ID for the request and set it on the envelope
                Guid requestId = Guid.NewGuid();
                Envelope env = new Envelope();
                env.SetMessageId(requestId);

                // add pattern & timeout information to the headers
                env.SetMessagePattern(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC);
                env.SetRpcTimeout(timeout);

                // create an event context
                MessageContext context = new MessageContext(MessageContext.Directions.Out, env, request);

                // let the outbound processor do its thing
                _eventProducer.ProcessMessage(context, () =>
                {
                    // create an RPC registration
                    RpcRegistration rpcRegistration = new RpcRegistration(requestId, expectedTopic, _eventConsumer.OpenEnvelope);

                    // register with the envelope bus
                    _envelopeBus.Register(rpcRegistration);

                    // now that we're setup to receive the response, send the request
                    _envelopeBus.Send(context.Envelope);

                    // get the envelope from the registraton
                    response = rpcRegistration.GetResponse(timeout);

                    // unregister from the bus
                    _envelopeBus.Unregister(rpcRegistration);
                });
            }
            catch (Exception ex)
            {
                Log.Error("Exception publishing an event", ex);
                throw;
            }

            Log.Debug("Leave GetResponseTo");
            return response;
        }

        public TResponse GetResponseTo<TResponse>(object request, TimeSpan timeout) where TResponse : class
        {
            string expectedTopic = typeof(TResponse).FullName;

            object responseObject = this.GetResponseTo(request, timeout, expectedTopic);

            return responseObject as TResponse;
        }

        public IEnumerable GatherResponsesTo(object request, TimeSpan timeout, params string[] expectedTopics)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<TResponse> GatherResponsesTo<TResponse>(object request, TimeSpan timeout) where TResponse : class
        {
            throw new NotImplementedException();
        }

        public void RespondTo(IDictionary<string, string> headers, object response)
        {
            Log.Debug("Enter RespondTo");

            if (null == response) { throw new ArgumentNullException("Cannot respond with a null event"); }
            if (null == headers) { throw new ArgumentNullException("Must provide non-null request headers"); }
            if (Guid.Empty == headers.GetMessageId()) { throw new ArgumentNullException("Cannot respond to a request because the provided request headers do not contain a message ID"); }

            try
            {
                Envelope env = new Envelope();
                env.SetCorrelationId(headers.GetMessageId());

                MessageContext context = new MessageContext(MessageContext.Directions.Out, env, response);

                _eventProducer.ProcessMessage(context, () =>
                {
                    _envelopeBus.Send(env);
                });
            }
            catch (Exception ex)
            {
                Log.Error("Exception responding to an event", ex);
                throw;
            }

            Log.Debug("Leave RespondTo");
        }
    }
}
