using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.commanding
{
    public class DefaultCommandBus : ICommandBus
    {
        private readonly ICommandSender _commandSender;
        private readonly ICommandReceiver _commandReceiver;


        public DefaultCommandBus(ICommandSender commandSender, ICommandReceiver commandReceiver)
        {
            _commandSender = commandSender;
            _commandReceiver = commandReceiver;
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
