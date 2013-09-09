using System;
using System.Collections.Generic;
using amp.messaging;
using cmf.bus;
using cmf.eventing;


namespace amp.eventing
{
    public class DefaultEventBus : IEventBus
    {
        protected readonly IEventConsumer _eventConsumer;
        protected readonly IEventProducer _eventProducer;

        public DefaultEventBus(IEventConsumer eventConsumer, IEventProducer eventProducer)
        {
            _eventConsumer = eventConsumer;
            _eventProducer = eventProducer;
        }

        public DefaultEventBus(IEnvelopeBus envelopeBus
            , List<IMessageProcessor> inboundChain
            , List<IMessageProcessor> outboundChain)
        {
            _eventConsumer = new DefaultEventConsumer(envelopeBus, inboundChain);
            _eventProducer = new DefaultEventProducer(envelopeBus, outboundChain);
        }

        public void Dispose()
        {
            _eventConsumer.Dispose();
            _eventProducer.Dispose();
        }

        public void Publish(object ev)
        {
            _eventProducer.Publish(ev);
        }

        public void Subscribe(IEventHandler handler)
        {
            _eventConsumer.Subscribe(handler);
        }

        public void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class
        {
            _eventConsumer.Subscribe(handler);
        }
    }
}
