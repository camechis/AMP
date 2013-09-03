using System;
using System.Collections.Generic;

namespace amp.commanding
{
    public interface ICommandHandler
    {
        Type CommandType { get; }

        object Handle(object command, IDictionary<string, string> headers);
    }
}
