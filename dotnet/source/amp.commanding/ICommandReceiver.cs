using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.commanding
{
    public interface ICommandReceiver : IDisposable
    {
        void ReceiveCommand(ICommandHandler handler);

        void ReceiveCommand<TCommand>(Action<TCommand, IDictionary<string, string>> handler) 
            where TCommand : class;
    }
}
