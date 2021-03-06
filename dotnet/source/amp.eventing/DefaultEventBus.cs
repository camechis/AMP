﻿using System;
using System.Collections.Generic;
using amp.messaging;
using cmf.bus;
using cmf.eventing;


namespace amp.eventing
{
    public class DefaultEventBus : IEventBus
    {
        protected readonly DefaultEventConsumer _eventConsumer;
        protected readonly DefaultEventProducer _eventProducer;

        public DefaultEventBus(IEnvelopeBus envelopeBus
            , List<IMessageProcessor> inboundChain
            , List<IMessageProcessor> outboundChain)
        {
            _eventConsumer = new DefaultEventConsumer(envelopeBus, inboundChain);
            _eventProducer = new DefaultEventProducer(envelopeBus, outboundChain);
        }

        public virtual void Dispose()
        {
            _eventConsumer.Dispose();
            _eventProducer.Dispose();
        }

        public virtual void Publish(object ev)
        {
            _eventProducer.Publish(ev);
        }

        public virtual void Publish(object ev, IDictionary<string, string> headers)
        {
            _eventProducer.Publish(ev, headers);    
        }


        public virtual void Subscribe(IEventHandler handler, Predicate<Envelope> envelopeFilter)
        {
            _eventConsumer.Subscribe(handler, envelopeFilter);
        }

        public virtual void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler, Predicate<Envelope> envelopeFilter) where TEvent : class
        {
            _eventConsumer.Subscribe(handler, envelopeFilter);
        }
    }
}
