using RabbitMQ.Client;

using amp.rabbit.topology;

namespace amp.rabbit
{
    public class BasicConnectionFactory : BaseConnectionFactory
    {
        protected string _username;
        protected string _password;


        public BasicConnectionFactory(string username = "guest", string password = "guest")
        {
            _username = username;
            _password = password;
        }


        protected override IConnection CreateConnection(Exchange exchange)
        {
            ConnectionFactory cf = new ConnectionFactory();
            cf.HostName = exchange.HostName;
            cf.VirtualHost = exchange.VirtualHost;
            cf.Port = exchange.Port;
            cf.UserName = _username;
            cf.Password = _password;

            return cf.CreateConnection();
        }
    }
}
