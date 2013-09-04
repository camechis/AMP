using System;
using System.Threading;
using cmf.eventing;
using NUnit.Framework;
using Spring.Context;
using Spring.Context.Support;

namespace amp.tests.integration.Eventing
{
    [TestFixture]
    public class PubSubTests
    {
        private IApplicationContext _context;
        private IEventBus _bus;

        [SetUp]
        public void Setup()
        {
            _context = new XmlApplicationContext(Constants.BASIC_AUTH_CONFIG);
            _bus = _context.GetObject("IRpcEventBus") as IEventBus;
        }

        [TearDown]
        public void TearDown()
        {
            _context.Dispose();
        }

        [Test]
        public void Should_be_able_to_publish_and_subscribe()
        {
            TestEvent receivedEvent = null;
            _bus.Subscribe<TestEvent>((@event, headers) =>
            {
                receivedEvent = @event;
            });

            TestEvent sentEvent = new TestEvent();

            _bus.Publish(sentEvent);

            var end = DateTime.Now.AddSeconds(5);
            while (receivedEvent == null || DateTime.Now > end)
            {
                Thread.Sleep(100);
            }

            Assert.That(receivedEvent, Is.Not.Null, "Event not received within timeout period.");
            Assert.That(receivedEvent.Id, Is.EqualTo(sentEvent.Id), "Received and Sent events were not the same.");
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
