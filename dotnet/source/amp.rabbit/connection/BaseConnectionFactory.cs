using System;
using System.Collections.Generic;
using amp.rabbit.topology;
using Common.Logging;
using RabbitMQ.Client;

namespace amp.rabbit.connection
{
    public abstract class BaseConnectionFactory : IRabbitConnectionFactory
    {
        protected ILog _log;
        protected IDictionary<Exchange, IConnectionManager> _connectionManagers;

        protected BaseConnectionFactory()
        {
            _connectionManagers = new Dictionary<Exchange, IConnectionManager>();
            _log = LogManager.GetLogger(this.GetType());
        }

        public IConnectionManager ConnectTo(Exchange exchange)
        {
            _log.Debug("Getting connection for exchange: " + exchange.ToString());
            IConnectionManager manager = null;

            // first, see if we have a cached connection
            if (_connectionManagers.ContainsKey(exchange))
            {
                manager = _connectionManagers[exchange];
            }
            else
            {
                _log.Debug("No connection to the exchange was cached: creating");
                manager = this.CreateConnectionManager(exchange);

                // add the new connection to the cache
                _connectionManagers[exchange] = manager;
            }

            return manager;
        }

        public void Dispose()
        {
            foreach (IConnectionManager conn in _connectionManagers.Values)
            {
                try { conn.Dispose(); }
                catch { }
            }
        }

        protected virtual IConnectionManager CreateConnectionManager(Exchange exchange)
        {
            _log.Debug("Enter CreateConnectionManager");

            ConnectionFactory cf = new ConnectionFactory();
            ConfigureConnectionFactory(cf, exchange);
            try
            {
                IConnectionManager connection = new ConnectionManager(cf);
                _log.Debug("Leave CreateConnectionManager");
                return connection;
            }
            catch (Exception e)
            {
                _log.Error("Unable to establish connection with RabbitMQ.", e);
                throw e;
            }
        }

        public virtual void ConfigureConnectionFactory(ConnectionFactory factory, Exchange exchange)
        {
            factory.HostName = exchange.HostName;
            factory.VirtualHost = exchange.VirtualHost;
            factory.Port = exchange.Port;
        }
    }
}