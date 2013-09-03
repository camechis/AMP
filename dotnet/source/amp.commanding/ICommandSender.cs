using System;

namespace amp.commanding
{
    public interface ICommandSender : IDisposable
    {
        void Send(object command);
    }
}
