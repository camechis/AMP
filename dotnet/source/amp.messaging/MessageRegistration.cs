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


        public Predicate<Envelope> Filter
        {
            get { return _filterPredicate; }
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
                String message = "Failed to process an incoming message envelope.";
                Log.Error(message, ex);
                throw new Exception(message, ex);
            }

            return null;
        }

        public object HandleFailed(Envelope env, Exception ex)
        {
            Log.Error("Failed to handle a message.", ex);
            return null;
        }

        public IDictionary<string, string> Info { get; set; }



        public MessageRegistration(Func<Envelope, MessageContext> envelopeOpener, IMessageHandler handler)
        {
            _envelopeOpener = envelopeOpener;
            _handler = handler;

            string handledType = _handler.Topic;

            this.Info = new Dictionary<string, string>
            {
                {EnvelopeHeaderConstants.MESSAGE_TOPIC, handledType},
                {EnvelopeHeaderConstants.MESSAGE_TYPE, handledType}
            };
        }

        public MessageRegistration(
            Func<Envelope, MessageContext> envelopeOpener,
            IMessageHandler handler,
            Predicate<Envelope> filterPredicate)
            : this(envelopeOpener, handler)
        {
            _filterPredicate = filterPredicate;
        }
    }
}
