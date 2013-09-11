using System;
using System.Collections.Generic;
using cmf.bus;
using Common.Logging;

namespace amp.messaging
{
    public class MessageRegistration : IRegistration
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageRegistration));

        private readonly IMessageHandler _handler;
        private readonly Predicate<Envelope> _filterPredicate;
        private readonly Func<Envelope, MessageContext> _envelopeOpener;
        private IDictionary<string, string> _info;


        public Predicate<Envelope> Filter
        {
            get { return _filterPredicate; }
        }

        public IDictionary<string, string> Info
        {
            get { return _info; }
        }

        public object Handle(Envelope env)
        {
            Log.Debug("Enter MessageRegistration # handle( Envelope env )");

            try {
                // use the provided envelope opener to get a context
                MessageContext context = _envelopeOpener(env);

                if (null != context)
                {
                    _handler.Handle(context.Message, context.Envelope.Headers);
                }
                else
                {
                    // if our opener returned null, that signifies that it declined
                    // to open it, and that we should quietly drop this envelope
                    // So, I'll return null here to make this explicit, even though
                    // just doing nothing here would lead to the 'return null' that
                    // is just a few lines below this.
                    return null;
                }
            }
            catch (MessageException ex) {
                const string message = "Failed to process an incoming message envelope.";
                Log.Error(message, ex);
                throw new Exception(message, ex);
            }

            return null;
        }

        public object HandleFailed(Envelope env, Exception ex)
        {
            Log.Debug("Enter HandleFailed");

            // either succeed & return the result or fail & return null
            try
            {
                object handleFailed = _handler.HandleFailed(env, ex);
                Log.Debug("Leave HandleFailed");
                return handleFailed;
            }
            catch (Exception failedToFail)
            {
                Log.Warn("Caught an exception attempting to handle the failed event", failedToFail);
                return null;
            }
        }

        public MessageRegistration(Func<Envelope, MessageContext> envelopeOpener, IMessageHandler handler)
            :this(envelopeOpener, handler, env => true)
        {
        }

        public MessageRegistration(
            Func<Envelope, MessageContext> envelopeOpener,
            IMessageHandler handler,
            Predicate<Envelope> filterPredicate)
        {
            _envelopeOpener = envelopeOpener;
            _handler = handler;
            _filterPredicate = filterPredicate;

            string handledType = _handler.Topic;

            _info = new Dictionary<string, string>
            {
                {EnvelopeHeaderConstants.MESSAGE_TOPIC, handledType},
                {EnvelopeHeaderConstants.MESSAGE_TYPE, handledType}
            };
        }
    }
}
