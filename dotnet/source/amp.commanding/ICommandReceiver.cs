using System;
using System.Collections.Generic;

namespace amp.commanding
{
    public interface ICommandReceiver : IDisposable
    {
        void ReceiveCommand(ICommandHandler handler);

        void ReceiveCommand<TCommand>(Action<TCommand, IDictionary<string, string>> handler) 
            where TCommand : class;
    }
}
