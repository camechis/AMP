using System;
using amp.rabbit.topology;
using RabbitMQ.Client;

namespace amp.rabbit.connection
{
    public interface IRabbitConnectionFactory : IDisposable
    {
        ConnectionManager ConnectTo(Exchange exchange);
    }
}
