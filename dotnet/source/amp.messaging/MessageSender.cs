using System;
using System.Collections.Generic;
using cmf.bus;
using Common.Logging;

namespace amp.messaging
{
    public class MessageSender
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageSender));

        private readonly IEnvelopeSender _envelopeSender;
        private readonly IMessageProcessor _messageProcessor;
        

        public MessageSender(IEnvelopeSender envelopeSender)
        {
            _envelopeSender = envelopeSender;
        }

        public MessageSender(IEnvelopeSender envelopeSender, IMessageProcessor messageProcessor)
            : this(envelopeSender)
        {
            _messageProcessor = messageProcessor;
        }

        public MessageSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processingChain)
            : this (envelopeSender, new MessageProcessorChain(processingChain))
        {
        }

        public void Send(object message)
        {
            this.Send(message, null);
        }

        public void Send(object message, IDictionary<string, string> headers)
        {
            // create an envelope for the message
            Envelope newEnvelope = new Envelope();
            newEnvelope.Headers = headers ?? new Dictionary<string, string>();

            // create a message context for message processing
            MessageContext ctx = new MessageContext(
                MessageContext.Directions.Out, newEnvelope, message);

            // process the message
            this.ProcessMessage(ctx, () =>
            {
                try
                {
                    _envelopeSender.Send(ctx.Envelope);
                }
                catch (Exception ex)
                {
                    string msg = "Failed to send an envelope.";
                    Log.Error(msg, ex);
                    throw new MessageException(msg, ex);
                }
            });
        }


        public void ProcessMessage(
            MessageContext context,
            Action onComplete)
        {
            _messageProcessor.ProcessMessage(context, onComplete);
        }

        public void Dispose()
        {
            _messageProcessor.Dispose();
        }
    }
}
