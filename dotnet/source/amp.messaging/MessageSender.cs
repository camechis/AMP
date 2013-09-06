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

        private IEnvelopeSender _envelopeSender;
        private IList<IMessageProcessor> _processingChain;


        public MessageSender(IEnvelopeSender envelopeSender)
        {
            _envelopeSender = envelopeSender;
        }

        public MessageSender(IEnvelopeSender envelopeSender, IList<IMessageProcessor> processingChain)
            : this (envelopeSender)
        {
            _processingChain = processingChain;
        }


        public void Send(object command)
        {
            // create an envelope for the command
            Envelope newEnvelope = new Envelope();

            // create a command context for command processing
            MessageContext ctx = new MessageContext(
                MessageContext.Directions.Out, newEnvelope, command);

            // process the command
            this.ProcessCommand(ctx, _processingChain, () =>
            {
                try
                {
                    _envelopeSender.Send(ctx.Envelope);
                }
                catch (Exception ex)
                {
                    string msg = "Failed to send a command envelope.";
                    Log.Error(msg, ex);
                    throw new MessageException(msg, ex);
                }
            });
        }

        public void ProcessCommand(
            MessageContext context,
            IEnumerable<IMessageProcessor> processingChain,
            Action onComplete)
        {
            Log.Debug("Enter processCommand");

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
                this.ProcessCommand(context, processingChain.Skip(1), onComplete);
            });

            Log.Debug("Leave processCommand");
        }

        public void Dispose()
        {
            _processingChain.ToList().ForEach(p =>
            {
                try { p.Dispose(); }
                catch { }
            });
        }
    }
}
