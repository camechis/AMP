using System;
using System.Collections.Generic;
using Common.Logging;

namespace amp.messaging
{
    public class MessageBus 
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(MessageBus));

        private readonly MessageSender _messageSender;
        private readonly MessageReceiver _messageReceiver;

        public MessageBus(MessageSender messageSender, MessageReceiver messageReceiver)
        {
            _messageSender = messageSender;
            _messageReceiver = messageReceiver;
        }


        public void Send(object message)
        {
            _messageSender.Send(message);
        }

        public void Dispose()
        {
            try
            {
                _messageSender.Dispose();
            }
            catch(Exception ex)
            {
                Log.Warn("Exception thrown while disposing of MessageSender", ex);
            }

            try
            {
                _messageReceiver.Dispose();
            }
            catch (Exception ex)
            {
                Log.Warn("Exception thrown while disposing of MessageReceiver", ex);
            }
        }

        public void ReceiveMessage(IMessageHandler handler)
        {
            _messageReceiver.ReceiveMessage(handler);
        }

        public void ReceiveMessage<TCommand>(Action<TCommand, IDictionary<string, string>> handler) where TCommand : class
        {
            _messageReceiver.ReceiveMessage(handler);
        }
    }
}
