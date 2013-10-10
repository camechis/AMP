using amp.rabbit.topology;
using RabbitMQ.Client;

namespace amp.rabbit.connection
{
    public class SslConnectionFactory : BaseConnectionFactory
    {
        public override void ConfigureConnectionFactory(ConnectionFactory factory, Exchange exchange)
        {
            base.ConfigureConnectionFactory(factory, exchange);

            factory.Ssl.ServerName = exchange.HostName;
            factory.Ssl.Enabled = true;
        }
    }
}