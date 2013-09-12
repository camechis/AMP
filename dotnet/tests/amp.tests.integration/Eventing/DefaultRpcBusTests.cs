using System;
using System.Collections.Generic;
using System.Linq;
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

            _backendContext = new XmlApplicationContext(ConfigFiles);
            _backendBus = _backendContext.GetObject("IRpcEventBus") as IRpcEventBus;

            _backendBus.Subscribe<TestRequest>(
                (@event, headers) =>
                    {
                        for (int i = 0; i < @event.ResponseCount; i++)
                            _backendBus.RespondTo(headers, new TestResponse { Id = @event.Id });

                    }, 
                env => true);
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

            TestRequest request = new TestRequest();

            TestResponse response = _rpcBus.GetResponseTo<TestResponse>(request, TimeSpan.FromSeconds(5));

            Assert.That(response, Is.Not.Null, "RPC Response not received within timeout period.");
            Assert.That(response.Id, Is.EqualTo(request.Id), "The RPC Response did not match the request.");
        }

        [Test]
        [Ignore("GatherResponsesTo is not implemented yet.")]
        public void Should_be_able_to_send_and_gather_via_rpc()
        {
            TestRequest request = new TestRequest{ResponseCount = 2};

            IEnumerable<TestResponse> responses = _rpcBus.GatherResponsesTo<TestResponse>(request, TimeSpan.FromSeconds(5));

            Assert.That(responses, Is.Not.Null, "RPC Response not received within timeout period.");
            TestResponse[] testResponses = responses.ToArray();
            Assert.That(testResponses.Length, Is.EqualTo(2), "The wrong number of responses were gathered.");
            foreach (var response in testResponses)
            {
                Assert.That(response.Id, Is.EqualTo(request.Id), "The RPC Response did not match the request.");
            }
        }

        public class TestRequest : TestEvent
        {
            public int ResponseCount { get; set; }

            public TestRequest()
            {
                ResponseCount = 1;
            }
        }
        public class TestResponse : TestEvent
        {

        }
    }
}
