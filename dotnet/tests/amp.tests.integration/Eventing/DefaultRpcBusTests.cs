using System;
using cmf.eventing.patterns.rpc;
using NUnit.Framework;
using Spring.Context;
using Spring.Context.Support;

namespace amp.tests.integration.Eventing
{
    public class DefaultRpcBusTests : DefaultEventBusTests
    {
        private IRpcEventBus _rpcBus;

        private IApplicationContext _backendContext;
        private IRpcEventBus _backendBus;

        [TestFixtureSetUp]
        public override void TestFixtureSetUp()
        {
            base.TestFixtureSetUp();
            _bus = _rpcBus = _context.GetObject("IRpcEventBus") as IRpcEventBus;

            _backendContext = new XmlApplicationContext(Constants.BASIC_AUTH_CONFIG);
            _backendBus = _backendContext.GetObject("IRpcEventBus") as IRpcEventBus;
        }

        [TestFixtureTearDown]
        public override void TestFixtureTearDown()
        {
            _backendContext.Dispose();
            base.TestFixtureTearDown();
        }

        [Test]
        public void Should_be_able_to_send_and_receive_via_rpc()
        {
            Assert.AreNotSame(_rpcBus, _backendBus);

            _backendBus.Subscribe<TestRequest>((@event, headers) =>
            {
                _backendBus.RespondTo(headers, new TestResponse { Id = @event.Id });

            });

            TestRequest request = new TestRequest();

            TestResponse response = _rpcBus.GetResponseTo<TestResponse>(request, TimeSpan.FromSeconds(10));

            Assert.That(response, Is.Not.Null, "RPC Response not received within timeout period.");
            Assert.That(response.Id, Is.EqualTo(request.Id), "The RPC Response did not match the request.");
        }

        public class TestRequest : TestEvent
        {

        }
        public class TestResponse : TestEvent
        {

        }
    }
}
