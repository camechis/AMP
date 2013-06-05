using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using cmf.bus;
using Common.Logging;

namespace amp.commanding
{
    public class DefaultCommandReceiver : ICommandReceiver
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(DefaultCommandReceiver));

        private IEnvelopeReceiver _envelopeReceiver;
        private IList<ICommandProcessor> _processingChain;


        public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver)
        {
            _envelopeReceiver = envelopeReceiver;
        }

        public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver, IList<ICommandProcessor> processingChain)
            : this(envelopeReceiver)
        {
            _processingChain = processingChain;
        }


        public void ReceiveCommand(ICommandHandler handler)
        {
            Log.Debug("Enter ReceiveCommand");
            if (null == handler) { throw new ArgumentNullException("Cannot register a null handler"); }

            // create a registration object
            CommandRegistration registration = new CommandRegistration(this.OpenEnvelope, handler);

            // and register it with the envelope receiver
            try {
                _envelopeReceiver.Register(registration);
            }
            catch (Exception ex) {
                String message = "Failed to register for a command";
                Log.Error(message, ex);
                throw new CommandException(message, ex);
            }
        }

        public void ReceiveCommand<TCommand>(Action<TCommand, IDictionary<string, string>> handler) where TCommand : class
        {
            this.ReceiveCommand(new TypedCommandHandler<TCommand>(handler));
        }

        public CommandContext OpenEnvelope(Envelope envelope)
        {
            // create a context for processing
            CommandContext ctx = new CommandContext(CommandContext.Directions.In, envelope);

            // a marker that indicates the processing result 
            bool isOpen = false;

            try
            {
                this.ProcessCommand(ctx, _processingChain, () => {
                    isOpen = true;
                });
            }
            catch(Exception ex)
            {
                string msg = "Failed to open a command envelope.";
                Log.Error(msg, ex);
                throw new CommandException(msg, ex);
            }

            // if we successfully opened the envelope, 
            return isOpen ? ctx : null;
        }

        public void Dispose()
        {
            _processingChain.ToList().ForEach(p =>
            {
                try { p.Dispose(); }
                catch { }
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
    }
}
