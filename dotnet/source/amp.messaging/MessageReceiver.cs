using System;
using System.Collections.Generic;
using cmf.bus;
using Common.Logging;

namespace amp.messaging
{
    public class MessageReceiver : IDisposable
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageReceiver));

        private readonly IEnvelopeReceiver _envelopeReceiver;
        private readonly IMessageProcessor _messageProcessor;
        

        public MessageReceiver(IEnvelopeReceiver envelopeReceiver)
        {
            _envelopeReceiver = envelopeReceiver;
        }

        public MessageReceiver(IEnvelopeReceiver envelopeReceiver, IMessageProcessor messageProcessor)
            : this(envelopeReceiver)
        {
            _messageProcessor = messageProcessor;
        }

        public MessageReceiver(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processingChain)
            : this(envelopeReceiver, new MessageProcessorChain(processingChain))
        {
        }


        public void ReceiveMessage(IMessageHandler handler, Predicate<Envelope> envelopeFilter)
        {
            Log.Debug("Enter ReceiveMessage");
            if (null == handler) { throw new ArgumentNullException("Cannot register a null handler"); }

            // create a registration object
            MessageRegistration registration = new MessageRegistration(this.OpenEnvelope, handler, envelopeFilter);

            // and register it with the envelope receiver
            try {
                _envelopeReceiver.Register(registration);
            }
            catch (Exception ex) {
                const string message = "Failed to register for a message";
                Log.Error(message, ex);
                throw new MessageException(message, ex);
            }
        }

        public void ReceiveMessage<TMessage>(Action<TMessage, IDictionary<string, string>> handler, Predicate<Envelope> envelopeFilter) where TMessage : class
        {
            this.ReceiveMessage(new TypedMessageHandler<TMessage>(handler), envelopeFilter);
        }

        public MessageContext OpenEnvelope(Envelope envelope)
        {
            // create a context for processing
            MessageContext ctx = new MessageContext(MessageContext.Directions.In, envelope);

            // a marker that indicates the processing result 
            bool isOpen = false;

            try
            {
                _messageProcessor.ProcessMessage(ctx, () => {
                    isOpen = true;
                });
            }
            catch(Exception ex)
            {
                const string msg = "Failed to open an envelope.";
                Log.Error(msg, ex);
                throw new MessageException(msg, ex);
            }

            // if we successfully opened the envelope, 
            return isOpen ? ctx : null;
        }

        public void Dispose()
        {
            _messageProcessor.Dispose();
            _envelopeReceiver.Dispose();
        }
    }
}
