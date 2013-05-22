using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.commanding
{
    public interface ICommandHandler
    {
        Type CommandType { get; }

        object Handle(object command, IDictionary<string, string> headers);
    }
}
