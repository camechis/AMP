using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;
using Common.Logging;

namespace amp.commanding
{
    public class DefaultCommandSender : ICommandSender
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(DefaultCommandSender));

        private IEnvelopeSender _envelopeSender;
        private IList<ICommandProcessor> _processingChain;


        public DefaultCommandSender(IEnvelopeSender envelopeSender)
        {
            _envelopeSender = envelopeSender;
        }

        public DefaultCommandSender(IEnvelopeSender envelopeSender, IList<ICommandProcessor> processingChain)
            : this (envelopeSender)
        {
            _processingChain = processingChain;
        }


        public void Send(object command)
        {
            // create an envelope for the command
            Envelope newEnvelope = new Envelope();

            // create a command context for command processing
            CommandContext ctx = new CommandContext(
                CommandContext.Directions.Out, newEnvelope, command);

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
                    throw new CommandException(msg, ex);
                }
            });
        }

        public void ProcessCommand(
            CommandContext context,
            IEnumerable<ICommandProcessor> processingChain,
            Action onComplete)
        {
            Log.Debug("Enter processCommand");

            // if the chain is null or empty, complete processing
            if ((null == processingChain) || (!processingChain.Any()))
            {
                Log.Debug("Command processing complete");
                onComplete();
                return;
            }


            // get the first processor
            ICommandProcessor processor = processingChain.First();

            // create a processing chain that no longer contains this processor
            IEnumerable<ICommandProcessor> newChain = processingChain.Skip(1);

            // let it process the command and pass its "next" processor: a method that
            // recursively calls this function with the current processor removed
            processor.ProcessCommand(context, () =>
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
