using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;
using cmf.bus;

namespace amp.commanding
{
    public class CommandRegistration : IRegistration
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(CommandRegistration));

        private readonly ICommandHandler _handler;
        private readonly Predicate<Envelope> _filterPredicate;
        private readonly Func<Envelope, CommandContext> _envelopeOpener;


        public Predicate<Envelope> Filter
        {
            get { return _filterPredicate; }
        }

        public object Handle(Envelope env)
        {
            Log.Debug("Enter CommandRegistration # handle( Envelope env )");

            try {
                // use the provided envelope opener to get a context
                CommandContext context = _envelopeOpener(env);

                if (null != context)
                {
                    _handler.Handle(context.Command, context.Envelope.Headers);
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
            catch (CommandException ex) {
                String message = "Failed to process an incoming command envelope.";
                Log.Error(message, ex);
                throw new Exception(message, ex);
            }

            return null;
        }

        public object HandleFailed(Envelope env, Exception ex)
        {
            Log.Error("Failed to handle a command.", ex);
            return null;
        }

        public IDictionary<string, string> Info { get; set; }



        public CommandRegistration(Func<Envelope, CommandContext> envelopeOpener, ICommandHandler handler)
        {
            _envelopeOpener = envelopeOpener;
            _handler = handler;

            string handledType = _handler.CommandType.FullName;

            this.Info = new Dictionary<string, string>
            {
                {EnvelopeHeaderConstants.MESSAGE_TOPIC, handledType},
                {EnvelopeHeaderConstants.MESSAGE_TYPE, handledType}
            };
        }

        public CommandRegistration(
            Func<Envelope, CommandContext> envelopeOpener,
            ICommandHandler handler,
            Predicate<Envelope> filterPredicate)
            : this(envelopeOpener, handler)
        {
            _filterPredicate = filterPredicate;
        }
    }
}
