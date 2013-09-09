using System;
using System.Collections.Generic;
using amp.messaging;
using cmf.bus;
using Common.Logging;

namespace amp.commanding
{
    public class DefaultCommandReceiver : MessageReceiver, ICommandReceiver
    {
        public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver) 
            : base(envelopeReceiver)
        {
        }

        public DefaultCommandReceiver(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processingChain) 
            : base(envelopeReceiver, processingChain)
        {
        }

        public void ReceiveCommand<TCommand>(Action<TCommand, IDictionary<string, string>> handler) where TCommand : class
        {
            base.ReceiveMessage(handler);
        }

        public void ReceiveCommand(ICommandHandler handler)
        {
            base.ReceiveMessage(new CommandMessageHandler(handler));
        }

        private class CommandMessageHandler : IMessageHandler
        {
            private static readonly ILog Log = LogManager.GetLogger(typeof(CommandMessageHandler));

            private readonly ICommandHandler _handler;
      
            public CommandMessageHandler(ICommandHandler handler)
            {
                _handler = handler;
            }

            public string Topic
            {
                get { return _handler.CommandType.FullName; }
            }

            public object Handle(object message, IDictionary<string, string> headers)
            {
                return _handler.Handle(message, headers);
            }

            public object HandleFailed(Envelope env, Exception ex)
            {
                Log.Warn("Failed to handle envelope.", ex);
                return null;
            }
        }
    }
}
