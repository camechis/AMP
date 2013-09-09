using System;
using System.Collections.Generic;
using amp.messaging;
using cmf.bus;
using cmf.eventing;

namespace amp.eventing
{
    public class DefaultEventConsumer : MessageReceiver, IEventConsumer
    {
        public DefaultEventConsumer(IEnvelopeReceiver envelopeReceiver) 
            : base(envelopeReceiver)
        {
        }

        public DefaultEventConsumer(IEnvelopeReceiver envelopeReceiver, List<IMessageProcessor> processingChain) 
            : base(envelopeReceiver, processingChain)
        {
        }


        public void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class
        {
            base.ReceiveMessage(handler);
        }

        public void Subscribe(IEventHandler handler)
        {
            base.ReceiveMessage(new EventMessageHandler(handler));
        }

        private class EventMessageHandler : IMessageHandler
        {
            private readonly IEventHandler _handler;

            public EventMessageHandler(IEventHandler handler)
            {
                _handler = handler;
            }

            public string Topic
            {
                get { return _handler.Topic; }
            }

            public object Handle(object message, IDictionary<string, string> headers)
            {
                return _handler.Handle(message, headers);
            }

            public object HandleFailed(Envelope env, Exception ex)
            {
                return _handler.HandleFailed(env, ex);
            }
        }
    }
}
