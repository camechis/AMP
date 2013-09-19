using System;
using System.Threading;
using amp.rabbit;
using amp.rabbit.topology;
using cmf.eventing;
using Moq;
using NUnit.Framework;
using RabbitMQ.Client;
using Spring.Context;
using Spring.Context.Support;

namespace amp.tests.integration.Rabbit
{
    [TestFixture]
    [Ignore("FIXME:  Need to implement amp.utility.http.SslWebRequestFactory!")]
    public class TokenConnectionFactoryTests
    {
        protected IApplicationContext _context;
        protected TokenConnectionFactory _factory;

        [TestFixtureSetUp]
        public virtual void TestFixtureSetUp()
        {
            _context = new XmlApplicationContext(Config.Authorization.AnubisBasic, Config.Topology.Simple);
            _factory = _context.GetObject("IRabbitConnectionFactory") as TokenConnectionFactory;
        }

        [TestFixtureTearDown]
        public virtual void TestFixtureTearDown()
        {
            _context.Dispose();
        }

        [Test]
        public void Should_be_able_to_get_token_from_Anubis()
        {
            var rabbitFactory = new Mock<ConnectionFactory>();
            _factory.ConfigureConnectionFactory(rabbitFactory.Object, new Exchange());

            rabbitFactory.VerifySet(f => f.UserName != null);
            rabbitFactory.VerifySet(f => f.Password != null);
        }

        public class TestEvent
        {
            public Guid Id { get; set; }

            public TestEvent()
            {
                Id = Guid.NewGuid();
            }
        }
    }
}
