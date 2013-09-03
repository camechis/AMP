using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RabbitMQ.Client;

using amp.rabbit.topology;

namespace amp.rabbit
{
    public interface IRabbitConnectionFactory : IDisposable
    {
        IConnection ConnectTo(Exchange exchange);
    }
}
