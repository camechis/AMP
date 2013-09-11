using System;
using System.Collections.Generic;
using cmf.bus;

namespace amp.messaging
{
    public interface IMessageHandler
    {
        string Topic { get; }

        object Handle(object message, IDictionary<string, string> headers);

        object HandleFailed(Envelope env, Exception ex);
    }
}
