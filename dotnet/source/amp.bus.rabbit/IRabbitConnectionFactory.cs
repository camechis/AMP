using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RabbitMQ.Client;

using amp.bus.rabbit.topology;

namespace amp.bus.rabbit
{
    public interface IRabbitConnectionFactory : IDisposable
    {
        IConnection ConnectTo(Exchange exchange);
    }
}
