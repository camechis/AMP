using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.commanding
{
    public interface ICommandSender : IDisposable
    {
        void Send(object command);
    }
}
