using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RabbitMQ.Client;

namespace amp.rabbit.connection
{
    /// <summary>
    /// Wrapps and manages a single connection, reconnecting if it fails.
    /// </summary>
    public class ConnectionManager : IDisposable
    {
        private ConnectionFactory _factory;
        private IConnection _connection;

        public ConnectionManager(ConnectionFactory factory)
        {
            _factory = factory;
            _connection = _factory.CreateConnection();
        }

        public IModel CreateModel()
        {
            return _connection.CreateModel();
        }

        public void Dispose()
        {
            if (_connection != null)
                _connection.Dispose();
        }
    }
}
