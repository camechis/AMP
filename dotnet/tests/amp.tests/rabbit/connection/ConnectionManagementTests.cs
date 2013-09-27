using System.Collections.Generic;

using Moq;
using NUnit.Framework;
using RabbitMQ.Client;

using amp.messaging;
using amp.rabbit.connection;
using amp.rabbit.topology;
using amp.rabbit.transport;
using cmf.bus;

namespace amp.tests.rabbit.connection
{
    [TestFixture]
    public class ConnectionManagementTests
    {
        private IList<Mock<IConnection>> _connections;
        private IList<Mock<IModel>> _models;
        private Mock<ConnectionFactory> _rmqFactory;
        
        private RabbitTransportProvider _transport;

        [SetUp]
        public void Setup()
        {
            _connections = new List<Mock<IConnection>>();
            _models = new List<Mock<IModel>>();

            _rmqFactory = new Mock<ConnectionFactory>();
            
            _rmqFactory.Setup(r => r.CreateConnection()).Returns(() =>
            {
                var connection = new Mock<IConnection>();
                _connections.Add(connection);

                connection.Setup(c => c.CreateModel()).Returns(() =>
                {
                    var model = new Mock<IModel>();
                    _models.Add(model);

                    model.Setup(m => m.CreateBasicProperties()).Returns(() =>
                    {
                        return new Mock<IBasicProperties>().Object;
                    });
                    
                    return model.Object;
                });

                return connection.Object;
            });

            _transport = new RabbitTransportProvider(
                new SimpleTopologyService(null, "test", "nowhere.com", "/", 0),
                new TestConnectionFactory(_rmqFactory.Object),
                new SimpleRoutingInfoCache(100));
        }

        [Test]
        public void Send_should_create_a_connection_and_a_channel()
        {
            var env = new Envelope();
            env.Headers.Add(EnvelopeHeaderConstants.MESSAGE_TOPIC, "testing");

            _transport.Send(env);

            _rmqFactory.Verify(r => r.CreateConnection(), "Connection to RabbitMQ was not created.");
            _connections[0].Verify(c => c.CreateModel(), "Channel for sent was not created.");
        }

        private class TestConnectionFactory : BaseConnectionFactory
        {
            readonly ConnectionFactory _factory;
            
            public TestConnectionFactory(ConnectionFactory factory)
            {
                _factory = factory;
            }

            protected override ConnectionManager CreateConnectionManager(Exchange exchange)
            {
                return new ConnectionManager(_factory);    
            }
        }
    }
}
