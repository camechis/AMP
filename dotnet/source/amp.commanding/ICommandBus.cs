using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.commanding
{
    public interface ICommandBus : ICommandSender, ICommandReceiver
    {
    }
}
