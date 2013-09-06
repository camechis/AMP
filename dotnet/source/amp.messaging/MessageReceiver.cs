﻿using System;
using System.Collections.Generic;
using System.Linq;
using cmf.bus;
using Common.Logging;

namespace amp.messaging
{
    public class MessageReceiver 
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageReceiver));

        private IEnvelopeReceiver _envelopeReceiver;
        private IList<IMessageProcessor> _processingChain;


        public MessageReceiver(IEnvelopeReceiver envelopeReceiver)
        {
            _envelopeReceiver = envelopeReceiver;
        }

        public MessageReceiver(IEnvelopeReceiver envelopeReceiver, IList<IMessageProcessor> processingChain)
            : this(envelopeReceiver)
        {
            _processingChain = processingChain;
        }


        public void ReceiveMessage(IMessageHandler handler)
        {
            Log.Debug("Enter ReceiveMessage");
            if (null == handler) { throw new ArgumentNullException("Cannot register a null handler"); }

            // create a registration object
            MessageRegistration registration = new MessageRegistration(this.OpenEnvelope, handler);

            // and register it with the envelope receiver
            try {
                _envelopeReceiver.Register(registration);
            }
            catch (Exception ex) {
                String message = "Failed to register for a command";
                Log.Error(message, ex);
                throw new MessageException(message, ex);
            }
        }

        public void ReceiveMessage<TMessage>(Action<TMessage, IDictionary<string, string>> handler) where TMessage : class
        {
            this.ReceiveMessage(new TypedMessageHandler<TMessage>(handler));
        }

        public MessageContext OpenEnvelope(Envelope envelope)
        {
            // create a context for processing
            MessageContext ctx = new MessageContext(MessageContext.Directions.In, envelope);

            // a marker that indicates the processing result 
            bool isOpen = false;

            try
            {
                this.ProcessMessage(ctx, _processingChain, () => {
                    isOpen = true;
                });
            }
            catch(Exception ex)
            {
                string msg = "Failed to open a command envelope.";
                Log.Error(msg, ex);
                throw new MessageException(msg, ex);
            }

            // if we successfully opened the envelope, 
            return isOpen ? ctx : null;
        }

        public void Dispose()
        {
            if(_processingChain != null)
                foreach (var processor in _processingChain)
                {
                    try { processor.Dispose(); }
                    catch { }
                }
        }

        public void ProcessMessage(
            MessageContext context,
            IEnumerable<IMessageProcessor> processingChain,
            Action onComplete)
        {
            Log.Debug("Enter processMessage");

            // if the chain is null or empty, complete processing
            if ((null == processingChain) || (!processingChain.Any()))
            {
                Log.Debug("Message processing complete");
                onComplete();
                return;
            }


            // get the first processor
            IMessageProcessor processor = processingChain.First();

            // create a processing chain that no longer contains this processor
            IEnumerable<IMessageProcessor> newChain = processingChain.Skip(1);

            // let it process the command and pass its "next" processor: a method that
            // recursively calls this function with the current processor removed
            processor.ProcessMessage(context, () =>
            {
                this.ProcessMessage(context, processingChain.Skip(1), onComplete);
            });

            Log.Debug("Leave processMessage");
        }
    }
}
