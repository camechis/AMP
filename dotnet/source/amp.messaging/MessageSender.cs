using System;
using System.Collections.Generic;
using System.Linq;
using cmf.bus;
using Common.Logging;

namespace amp.messaging
{
    public class MessageSender 
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageSender));

        private readonly IEnvelopeSender _envelopeSender;
        private readonly List<IMessageProcessor> _processingChain;


        public MessageSender(IEnvelopeSender envelopeSender)
        {
            _envelopeSender = envelopeSender;
        }

        public MessageSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processingChain)
            : this (envelopeSender)
        {
            _processingChain = processingChain;
        }


        public void Send(object message)
        {
            // create an envelope for the message
            Envelope newEnvelope = new Envelope();

            // create a message context for message processing
            MessageContext ctx = new MessageContext(
                MessageContext.Directions.Out, newEnvelope, message);

            // process the message
            this.ProcessMessage(ctx, _processingChain, () =>
            {
                try
                {
                    _envelopeSender.Send(ctx.Envelope);
                }
                catch (Exception ex)
                {
                    string msg = "Failed to send a message envelope.";
                    Log.Error(msg, ex);
                    throw new MessageException(msg, ex);
                }
            });
        }

        public void ProcessMessage(
            MessageContext context,
            List<IMessageProcessor> processingChain,
            Action onComplete)
        {
            Log.Debug("Enter ProcessMessage");

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
            List<IMessageProcessor> newChain = processingChain.Skip(1).ToList();

            // let it process the message and pass its "next" processor: a method that
            // recursively calls this function with the current processor removed
            processor.ProcessMessage(context, () => this.ProcessMessage(context, newChain, onComplete));

            Log.Debug("Leave ProcessMessage");
        }

        public void Dispose()
        {
            _processingChain.ToList().ForEach(p =>
            {
                try { p.Dispose(); }
                catch (Exception ex) { Log.Warn("Exception disposing of processor " + p, ex); }
            });
        }
    }
}
