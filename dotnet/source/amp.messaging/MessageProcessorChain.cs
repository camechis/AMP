using System;
using System.Collections.Generic;
using System.Linq;
using Common.Logging;

namespace amp.messaging
{
    /// <summary>
    /// An IMessageProcessor implementation used to join multiple other IMessageProcessors
    /// in an ordered chain, so that the entire set can be treated as a single processor
    /// for referencing and processing purposes.
    /// </summary>
    /// <remarks>
    /// Use of this class eliminates the need to have chaining logic in more than 
    /// one location and also permits the same chain to be declared once and used multiply.
    /// </remarks>
    public class MessageProcessorChain : IMessageProcessor
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageSender));

        private readonly List<IMessageProcessor> _processingChain;

        public MessageProcessorChain(List<IMessageProcessor> processingChain)
        {
            _processingChain = processingChain;
        }

        public void ProcessMessage(
            MessageContext context,
            Action continueProcessing)
        {
            Log.Debug(string.Format("Enter ProcessMessage - Direction: {0}", context.Direction));
            ProcessMessage(context, _processingChain, continueProcessing);
            Log.Debug(string.Format("Leave ProcessMessage - Direction: {0}", context.Direction));
        }

        public void ProcessMessage(
            MessageContext context,
            List<IMessageProcessor> processingChain,
            Action continueProcessing)
        {
            // if the chain is null or empty, complete processing
            if ((null == processingChain) || (!processingChain.Any()))
            {
                Log.Debug(string.Format("Message processing complete. Direction: {0}", context.Direction));
                continueProcessing();
                return;
            }

            // get the first processor
            IMessageProcessor processor = processingChain.First();

            // create a processing chain that no longer contains this processor
            List<IMessageProcessor> newChain = processingChain.Skip(1).ToList();

            // let it process the message and pass its "next" processor: a method that
            // recursively calls this function with the current processor removed
            Log.Debug(string.Format("Handing message to processor: {0}  - Direction: {1}", processor.GetType(), context.Direction));
            processor.ProcessMessage(context, () => ProcessMessage(context, newChain, continueProcessing));
        }
        public void Dispose()
        {
            foreach (IMessageProcessor processor in _processingChain)
            {
                try
                {
                    processor.Dispose();
                }
                catch (Exception ex)
                {
                    Log.Warn(string.Format("Message processor of type {0} threw exception upon dispose.", processor.GetType()), ex);
                    throw;
                }
            }
        }
    }
}