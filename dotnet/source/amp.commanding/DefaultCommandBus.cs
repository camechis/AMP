using System;
using System.Collections.Generic;
using amp.messaging;
using cmf.bus;

namespace amp.commanding
{
    public class DefaultCommandBus : ICommandBus
    {
        protected readonly ICommandSender _commandSender;
        protected readonly ICommandReceiver _commandReceiver;


        public DefaultCommandBus(IEnvelopeBus envelopeBus
            , List<IMessageProcessor> inboundChain
            , List<IMessageProcessor> outboundChain)
        {
            _commandReceiver = new DefaultCommandReceiver(envelopeBus, inboundChain);
            _commandSender = new DefaultCommandSender(envelopeBus, outboundChain);
        }


        public void Send(object command)
        {
            _commandSender.Send(command);
        }

        public void Dispose()
        {
            try
            {
                _commandSender.Dispose();
            }
            catch
            {
            }

            try
            {
                _commandReceiver.Dispose();
            }
            catch
            {
            }
        }

        public void ReceiveCommand(ICommandHandler handler)
        {
            _commandReceiver.ReceiveCommand(handler);
        }

        public void ReceiveCommand<TCommand>(Action<TCommand, IDictionary<string, string>> handler) where TCommand : class
        {
            _commandReceiver.ReceiveCommand(handler);
        }
    }
}
